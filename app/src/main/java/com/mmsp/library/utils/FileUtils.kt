package com.mmsp.library.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.IOException

class FileUtils {
    companion object {

        fun createUploadTempFile(context: Context, extension: String): File {
            val cacheDicFile = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val outDir = File(cacheDicFile, "camera_photos")
            if (!outDir.exists()) {
                outDir.mkdirs()
            }
            val uploadImageName = "upload_image_${System.currentTimeMillis()}.${extension}"
            val outputFile = File(outDir, uploadImageName)
            try {
                if (outputFile.exists()) {
                    outputFile.delete()
                }
                outputFile.createNewFile()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            return outputFile
        }

    }
}