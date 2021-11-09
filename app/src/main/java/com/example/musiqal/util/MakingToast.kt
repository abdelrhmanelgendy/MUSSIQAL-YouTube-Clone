package com.example.musiqal.util

import android.content.Context
import android.widget.Toast

class MakingToast(val context: Context) {
    companion object
    {
        val LENGTH_LONG=1
        val LENGTH_SHORT=0
    }

    fun toast(msg:String,length:Int)
    {
        Toast.makeText(context,msg,length).show()
    }
}