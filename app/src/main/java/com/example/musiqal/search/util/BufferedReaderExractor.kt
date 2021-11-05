package com.example.musiqal.search.util

import java.io.BufferedReader
import java.lang.StringBuilder

object BufferedReaderExractor {

    public fun extractText(rd: BufferedReader): String {
        val sb = StringBuilder()
        var cp: Int
        while (rd.read().also { cp = it } != -1) {
            sb.append(cp.toChar())
        }
        return sb.toString()
    }
}