package com.example.musiqal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.musiqal.ui.MainActivity

class AttemptsActivity : AppCompatActivity() {
    companion object {
        var isFirstTime = true

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attempts)
    }

    fun firstOpen(view: View) {
        isFirstTime = true
        startActivity(Intent(this,MainActivity::class.java))
    }

    fun notFirstTime(view: View) {
        isFirstTime = false
        startActivity(Intent(this,MainActivity::class.java))
    }
}