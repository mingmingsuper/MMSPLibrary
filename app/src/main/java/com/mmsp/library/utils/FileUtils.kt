package com.mmsp.library.utils

import android.content.Context
import android.os.Environment
import java.io.File

class FileUtils {
    companion object {

        fun createUploadTempFile(context: Context): File {
            val cacheDicFile = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val outDir = File(cacheDicFile, "camera_photos") //"output_image.jpg"
            if (!outDir.exists()) {
                outDir.mkdirs()
            }
            val uploadImageName = "upload_image_${System.currentTimeMillis()}"
            val outputFile = File(outDir, uploadImageName)
            return outputFile
        }

    }
}