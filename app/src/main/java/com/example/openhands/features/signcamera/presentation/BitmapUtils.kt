package com.example.openhands.features.signcamera.presentation

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageProxy


fun ImageProxy.toBitmap(): Bitmap {
    val planeProxy = planes[0]
    val buffer = planeProxy.buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)

    val initialBitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

    val matrix = Matrix().apply {
        postRotate(imageInfo.rotationDegrees.toFloat())
    }

    return Bitmap.createBitmap(initialBitmap, 0, 0, initialBitmap.width, initialBitmap.height, matrix, true)
}