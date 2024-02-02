package com.condex.myfavouritespets.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileManager {

    fun getFile(context: Context, filePath: String): File {
        val petFiles = File(context.filesDir, filePath)
        if (!petFiles.exists()) {
            try {
                petFiles.createNewFile()
                println("El archivo se ha creado correctamente.")
            } catch (e: Exception) {
                println("Error al crear el archivo: ${e.message}")
            }
        } else {
            println("El archivo ya existe.")
        }
        return petFiles
    }

    fun saveBitmapToExternalStorage(bitmap: Bitmap, filename: String) {
        val externalStorageState = Environment.getExternalStorageState()
        if (externalStorageState == Environment.MEDIA_MOUNTED) {
            val storageDirectory = Environment.getExternalStorageDirectory().toString()
            val file = File(storageDirectory, filename)
            try {
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 85, outputStream)
                outputStream.flush()
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            // Handle the case where the external storage is not mounted
        }
    }

    fun saveBitmapToFile(context: Context, bitmap: Bitmap, filename: String): String? {
        var fileOutputStream: FileOutputStream? = null
        var result: String? = null
        try {
            // Use MODE_PRIVATE to make the created file only accessible by your app
            fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream)
            result = context.getFileStreamPath(filename).absoluteFile.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fileOutputStream?.flush()
                fileOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return result
    }

    fun loadBitmapFromFilePath(filePath: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(filePath)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}