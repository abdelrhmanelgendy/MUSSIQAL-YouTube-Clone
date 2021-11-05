package com.example.musiqal.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import dagger.hilt.android.AndroidEntryPoint

import android.view.View
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis



private const val TAG = "Test"
@AndroidEntryPoint
class Test : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.musiqal.R.layout.activity_test)

        CoroutineScope(Dispatchers.Default).launch{
            val time = measureTimeMillis {

                val  answer1=async {print1()}
                val  answer2=async {print2()}
                Log.d(TAG, "onCreate: "+answer1.await())
                Log.d(TAG, "onCreate: "+answer2.await())

            }
            Log.d(TAG, "onCreate: "+time)


        }


    }

    suspend fun print1(): Int {
        delay(2000)
        return 10
    }

    suspend fun print2(): Int {
        delay(2000)
        return 20
    }


}