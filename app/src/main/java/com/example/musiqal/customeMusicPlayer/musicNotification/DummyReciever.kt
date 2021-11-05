package com.example.musiqal.customeMusicPlayer.musicNotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import com.example.musiqal.ui.MainActivity

class DummyReciever :
    BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val intentAction = intent?.action
        Log.d("DummyReciever", "onReceive: " + intentAction)

    }
}