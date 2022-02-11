package com.example.musiqal.downloadManager.util

import java.text.DecimalFormat

object FileSizeFormat {
    fun formatFileSize(size: Long): String {
        var hrSize: String? = null
        val b = size.toDouble()
        val k = size / 1024.0
        val m = size / 1024.0 / 1024.0
        val g = size / 1024.0 / 1024.0 / 1024.0
        val t = size / 1024.0 / 1024.0 / 1024.0 / 1024.0
        val dec = DecimalFormat("0.00")
        hrSize = if (t > 1) {
            dec.format(t)+(" TB")
        } else if (g > 1) {
            dec.format(g)+(" GB")
        } else if (m > 1) {
            dec.format(m)+(" MB")
        } else if (k > 1) {
            dec.format(k)+(" KB")
        } else {
            dec.format(b)+(" Bytes")
        }
        return hrSize
    }
}