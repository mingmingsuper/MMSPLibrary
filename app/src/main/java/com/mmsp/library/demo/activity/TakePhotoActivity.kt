package com.mmsp.library.demo.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.mmsp.library.R
import com.mmsp.library.databinding.ActivityTakePhotoBinding
import com.mmsp.library.uikit.BaseActivity
import com.mmsp.library.utils.FileUri2PathUtils
import com.mmsp.library.utils.FileUtils
import com.mmsp.library.utils.PermissionUtils
import java.io.File

class TakePhotoActivity : BaseActivity<ActivityTakePhotoBinding>(), OnClickListener {

    private val tag = "TakePhotoActivity"
    private val requestCodeTakePhoto = 1000
    private val requestCodeSelectPhoto = 1001
    private val requestCodePermission = 1002
    private val requestCodePermissionGallery = 1003
    private val requestCodeCutting = 1004
    private val requestCodePermissionVideo = 1005
    private val requestCodeTakeVideo = 1006

    private var outputImage: File? = null
    private var outputVideo: File? = null

    override fun layoutId(): Int {
        return R.layout.activity_take_photo
    }

    override fun initView() {
        initToolbar(mBinding.toolbarLayout.toolbar)
        mBinding.takePhoto.setOnClickListener(this)
        mBinding.selectPhoto.setOnClickListener(this)
        mBinding.takeVideo.setOnClickListener(this)
        mBinding.selectVideo.setOnClickListener(this)
    }

    override fun initData() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.takePhoto -> {
                Log.d(tag, "takePhoto")
                takePhoto()
            }

            R.id.selectPhoto -> {
                Log.d(tag, "selectPhoto")
                selectPhoto()
            }

            R.id.takeVideo -> {
                takeVideo()
            }

            R.id.selectVideo -> {
                selectVideo()
            }
        }
    }

    private fun takePhoto() {

        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
//            val intent = Intent()
//            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
//            startActivity(intent)
//            return
//        }

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d(tag, "外部存储卡不可用")
            return
        }

        if (!PermissionUtils.checkPermission(this, permissions)) {
            PermissionUtils.requestPermission(this, permissions,  requestCodePermission)
            return
        }
        val outputFile = FileUtils.createUploadTempFile(this, "jpg")
        outputImage = outputFile

        if (outputFile.exists()) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, "$packageName.provider", outputFile))
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile))
            }
            startActivityForResult(intent, requestCodeTakePhoto)
        }
    }

    private fun selectPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionUtils.checkPermission(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))) {
                PermissionUtils.requestPermission(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), requestCodePermissionGallery)
            } else {
                openGallery()
            }
        } else {
           openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, null)
//        val intentVideo = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI) // 从相册获取视频
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intent, requestCodeSelectPhoto)

        //SAF访问
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.type = "image/*"
//        startActivityForResult(intent, 100)
    }

    private fun takeVideo() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d(tag, "外部存储卡不可用")
            return
        }

        if (!PermissionUtils.checkPermission(this, permissions)) {
            PermissionUtils.requestPermission(this, permissions,  requestCodePermissionVideo)
            return
        }
        val outputFile = FileUtils.createUploadTempFile(this, "mp4")
        outputVideo = outputFile

        if (outputFile.exists()) {
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, "$packageName.provider", outputFile))
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile))
            }

            startActivityForResult(intent, requestCodeTakeVideo)
        }
    }

    private fun selectVideo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionUtils.checkPermission(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))) {
                PermissionUtils.requestPermission(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), requestCodePermissionGallery)
            } else {
                openGalleryVideo()
            }
        } else {
            openGalleryVideo()
        }
    }

    private fun openGalleryVideo() {
        val intent = Intent(Intent.ACTION_PICK, null)
        intent.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*")
        startActivityForResult(intent, requestCodeSelectPhoto)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            requestCodeTakePhoto -> {
                Log.d(tag, "TAKE_PHOTO")
                if (resultCode == Activity.RESULT_OK) {
                    outputImage?.apply {
                        Log.e(tag, "outputImage:$absolutePath")
                        Glide.with(this@TakePhotoActivity).load(this).into(mBinding.imageView)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            val contentUri = FileProvider.getUriForFile(
                                this@TakePhotoActivity,
                                "$packageName.provider",
                                this
                            )
//                            cropPhoto(contentUri)
                        } else {
                            val uri = Uri.fromFile(this)
//                            cropPhoto(uri)
                        }
                    }
                }
            }

            requestCodeSelectPhoto -> {
                Log.d(tag, "SELECT_PHOTO")
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.apply {
                        Log.e(tag, "data:$this")
                        val path = FileUri2PathUtils.getFileAbsolutePath(this@TakePhotoActivity, this)
                        Glide.with(this@TakePhotoActivity).load(path).into(mBinding.imageView)
                    }
                }
            }

            requestCodeTakeVideo -> {
                if (resultCode == Activity.RESULT_OK) {
                    outputVideo?.apply {
                        Log.e(tag, "outputVideo:$absolutePath")
//                        Glide.with(this@TakePhotoActivity).load(this).into(mBinding.imageView)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            val contentUri = FileProvider.getUriForFile(
                                this@TakePhotoActivity,
                                "$packageName.provider",
                                this
                            )
                            Log.e(tag, "outputVideo Android N:${contentUri.path}")
                            Glide.with(this@TakePhotoActivity).load(contentUri).into(mBinding.imageView)
//                            cropPhoto(contentUri)
                        } else {
                            val uri = Uri.fromFile(this)
                            Log.e(tag, "outputVideo Small Android N:${uri.path}")
                            Glide.with(this@TakePhotoActivity).load(uri).into(mBinding.imageView)
//                            cropPhoto(uri)
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            requestCodePermission -> {
                if (PermissionUtils.checkPermission(this, permissions)) {
                    takePhoto()
                }
            }
            requestCodePermissionGallery -> {
                if (PermissionUtils.checkPermission(this, permissions)) {
                    selectPhoto()
                }
            }
        }
    }

    private fun cropPhoto(uri: Uri) {
        val intent = Intent("com.android.camera.action.CROP")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, requestCodeCutting);
    }
}

