package com.mmsp.library.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class FileUri2PathUtils {

    private static final String SCHEME_CONTENT = "content";
    private static final String SCHEME_FILE = "file";
    private static final String PRIMARY_STORAGE = "primary";
    private static final String EXT_STORAGE_DOC = "com.android.externalstorage.documents";
    private static final String DOWNLOADS_DOC = "com.android.providers.downloads.documents";
    private static final String MEDIA_DOC = "com.android.providers.media.documents";
    private static final String IMAGE = "image";
    private static final String VIDEO = "video";
    private static final String AUDIO = "audio";
    private static final String GOOGLE_PHOTOS = "com.google.android.apps.photos.content";

    /**
     * 根据Uri获取文件绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context  上下文
     * @param imageUri 文件Uri
     */
    public static String getFileAbsolutePath(Context context, Uri imageUri) {
        if (context == null || imageUri == null) {
            return null;
        }

        // 检查权限（对于API 23+）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                // 请求权限
                Log.e("FileUri2PathUtils", "Permission denied: READ_EXTERNAL_STORAGE");
                return null;
            }
        }

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return getRealFilePath(context, imageUri);
        }

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
                    && android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
                    && DocumentsContract.isDocumentUri(context, imageUri)) {
                return handleDocumentUri(context, imageUri);
            } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return uriToFileApiQ(context, imageUri);
            } else if (SCHEME_CONTENT.equalsIgnoreCase(imageUri.getScheme())) {
                return handleContentUri(context, imageUri);
            } else if (SCHEME_FILE.equalsIgnoreCase(imageUri.getScheme())) {
                return imageUri.getPath();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private static String handleDocumentUri(Context context, Uri uri) {
        String docId = DocumentsContract.getDocumentId(uri);
        if (isExternalStorageDocument(uri)) {
            return handleExternalStorageDocument(docId);
        } else if (isDownloadsDocument(uri)) {
            return handleDownloadsDocument(context, docId);
        } else if (isMediaDocument(uri)) {
            return handleMediaDocument(context, docId);
        }
        return null;
    }

    private static String handleExternalStorageDocument(String docId) {
        String[] split = docId.split(":");
        if (PRIMARY_STORAGE.equalsIgnoreCase(split[0])) {
            // 检查外部存储状态
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
        }
        return null;
    }

    private static String handleDownloadsDocument(Context context, String docId) {
        Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
        return getDataColumn(context, contentUri, null, null);
    }

    private static String handleMediaDocument(Context context, String docId) {
        String[] split = docId.split(":");
        String type = split[0];
        Uri contentUri = null;
        if (IMAGE.equals(type)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if (VIDEO.equals(type)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if (AUDIO.equals(type)) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        if (contentUri != null) {
            String selection = MediaStore.Images.Media._ID + "=?";
            String[] selectionArgs = new String[]{split[1]};
            return getDataColumn(context, contentUri, selection, selectionArgs);
        }
        return null;
    }

    private static String handleContentUri(Context context, Uri uri) {
        if (isGooglePhotosUri(uri)) {
            return uri.getLastPathSegment();
        }
        return getDataColumn(context, uri, null, null);
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return EXT_STORAGE_DOC.equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return DOWNLOADS_DOC.equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return MEDIA_DOC.equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return GOOGLE_PHOTOS.equals(uri.getAuthority());
    }

    //此方法 只能用于4.4以下的版本
    private static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null || ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            String[] projection = {MediaStore.Images.ImageColumns.DATA};
            try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
                if (null != cursor && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }

                }
            } catch (Exception e) {
                return null;
            }
        }
        return data;
    }


    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);) {
            if (null != cursor && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Android 10 以上适配
     *
     * @param context 上下文
     * @param uri     文件uri
     * @return 文件路径
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static String uriToFileApiQ(Context context, Uri uri) {
        if (uri == null || uri.getScheme() == null) {
            return null;
        }
        File file;
        String scheme = uri.getScheme();
        //android10以上转换
        if (scheme.equalsIgnoreCase(ContentResolver.SCHEME_FILE)) {
            String path = uri.getPath();
            if (path != null) {
                file = new File(path);
            } else {
                return null;
            }
        } else if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            file = copyContentUriToFile(context, uri);
        } else {
            return null;
        }
        return file.getAbsolutePath();
    }

    private static File copyContentUriToFile(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        File file = null;
        try {
            cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                String displayName = index >= 0 ? cursor.getString(index) : "unknown";
                file = createUniqueFile(context.getExternalCacheDir(), displayName);
                try (InputStream inputStream = contentResolver.openInputStream(uri);
                     BufferedInputStream input = new BufferedInputStream(inputStream);
                     FileOutputStream outputStream = new FileOutputStream(file);
                     BufferedOutputStream output = new BufferedOutputStream(outputStream)) {
                    byte[] buffer = new byte[4096];
                    int byteRead;
                    while ((byteRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, byteRead);
                    }
                }
            }
        } catch (IOException | SecurityException e) {
            Log.e("FileUtils", "copyContentUriToFile: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return file;
    }

    private static File createUniqueFile(File directory, String baseName) {
        String uniqueFileName = UUID.randomUUID().toString() + "_" + baseName;
        return new File(directory, uniqueFileName);
    }
}
