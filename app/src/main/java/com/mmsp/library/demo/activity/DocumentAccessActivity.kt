package com.mmsp.library.demo.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.core.view.WindowCompat
import com.mmsp.library.R
import com.mmsp.library.databinding.ActivityDocumentAccessBinding
import com.mmsp.library.uikit.BaseActivity

class DocumentAccessActivity : BaseActivity<ActivityDocumentAccessBinding>(), OnClickListener {

    private val requestCodeCreateDocument = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
//        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
//        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        enableEdgeToEdge()
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }

    override fun layoutId(): Int {
        return R.layout.activity_document_access
    }

    override fun initView() {
        initToolbar(mBinding.toolbarLayout.toolbar)
        mBinding.buttonCreateDocument.setOnClickListener(this)
    }

    override fun initData() {
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.button_create_document -> {
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_TITLE, "test.pdf")
//                    val dir =getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
//                    val dirPDF = File(dir, "pdfFiles")
//                    if (!dirPDF.exists()) {
//                        dirPDF.mkdirs()
//                    }
//                    putExtra(DocumentsContract.EXTRA_INITIAL_URI, dirPDF)
                }
                startActivityForResult(intent, requestCodeCreateDocument)
            }
        }
    }
}