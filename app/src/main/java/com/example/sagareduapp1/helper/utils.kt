package com.example.sagareduapp1.helper

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.IOException

fun getBitmapFromAssetsByIndex(context: Context, level: String, index: Int): ImageBitmap? {
    return try {
        val files = context.assets.list(level)
        if (files != null && index >= 0 && index < files.size) {
            val fileName = files[index]
            val fullPath = "$level/$fileName"
            context.assets.open(fullPath).use { inputStream ->
                BitmapFactory.decodeStream(inputStream).asImageBitmap()
            }
        } else {
            null
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun getAnswerFromFileName(context: Context, level: String, index: Int): String {
    return try {
        val files = context.assets.list(level)
        if (files != null && index >= 0 && index < files.size) {
            val fileName = files[index]
            // Format: level01_pic01_0.png -> answer is "0"
            // Split by '_' and then remove the extension
            val parts = fileName.split("_")
            if (parts.size >= 3) {
                parts[2].substringBefore(".")
            } else {
                ""
            }
        } else {
            ""
        }
    } catch (e: IOException) {
        ""
    }
}

fun getTotalImagesInLevel(context: Context, level: String): Int {
    return try {
        context.assets.list(level)?.size ?: 0
    } catch (e: IOException) {
        0
    }
}
