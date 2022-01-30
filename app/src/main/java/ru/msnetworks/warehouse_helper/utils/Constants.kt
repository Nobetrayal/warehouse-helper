package ru.msnetworks.warehouse_helper.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.core.graphics.scale

class Utils {
    companion object {
        private const val WIDTH_SCALED_SIZE = 1920

        private fun getScaledDimens(width: Int, height: Int): Pair<Int, Int> {
            val scaleFactor = width / WIDTH_SCALED_SIZE
            return if (scaleFactor == 0) {
                Pair(width, height)
            } else {
                Pair(width / scaleFactor, height / scaleFactor)
            }
        }

        private fun rotateBitmap(source: Bitmap, degrees: Float): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(degrees)
            return Bitmap.createBitmap(
                source, 0, 0, source.width, source.height, matrix, true
            )
        }

        fun getScaledBitmap(originalBitmap: Bitmap, isRotate: Boolean = false): Bitmap {
            val (w, h) = getScaledDimens(originalBitmap.width, originalBitmap.height)
            val newBitmap = originalBitmap.scale(w, h)
            return if (isRotate) {
                rotateBitmap(newBitmap, 90f)
            } else {
                newBitmap
            }
        }
    }
}