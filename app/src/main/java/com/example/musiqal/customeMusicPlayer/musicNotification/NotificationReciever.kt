package com.example.musiqal.customeMusicPlayer.musicNotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.view.KeyEvent
import androidx.media.session.MediaButtonReceiver
import com.example.musiqal.ui.MainActivity

class NotificationReciever :
    BroadcastReceiver() {
    private val TAG = "NotificationReciever"
    private val HEAD_SET_PLUGGED = 1
    private val HEAD_SET_UN_PLUGED = 0
    private val DOUBLE_TAP_DURATION=800

    companion object {
        lateinit var musicNotificationReceiver: MusicNotificationReceiverListener
        val listOfTimes = mutableListOf<Long>()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val intentAction = intent?.action


        Log.d(TAG, "onReceive: "+intentAction)
        when (intentAction) {
            MusicPlayerActions.ACTION_PLAY -> {
                musicNotificationReceiver.playPauseReciver()
            }
            MusicPlayerActions.ACTION_NEXT -> {
                musicNotificationReceiver.next()
            }
            MusicPlayerActions.ACTION_PREVIOUS -> {
                Log.d(TAG, "onReceive: previ")
                musicNotificationReceiver.previous()
            }
            MusicPlayerActions.ACTION_CLOSE -> {
                Log.d(TAG, "onReceive: previ")
                musicNotificationReceiver.stopPlaying()
            }
            Intent.ACTION_HEADSET_PLUG -> {
                headSetPlugging(intent.getIntExtra("state", -1))
            }
            Intent.ACTION_MEDIA_BUTTON -> {
                headSetActionButtonClicked(intent)
            }
        }
    }

    var hasSelectrd = false

    private fun headSetActionButtonClicked(intent: Intent) {
        var firstActionTime: Long = 0L
        var secondActionTime: Long = 0L

        val keyEvent = intent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)
        if (keyEvent?.action == 0) {
//            musicNotificationReceiver.playPauseReciver()
            listOfTimes.add(keyEvent.eventTime)

        }
        if (listOfTimes.size == 2) {
            if ((listOfTimes.get(1)-listOfTimes.get(0))<DOUBLE_TAP_DURATION) {

                Log.d(TAG, "headSetActionButtonClicked: double taped")
            }
        }

        clearListOfEvents()


    }

    private fun clearListOfEvents() {
        if (listOfTimes.size >= 2) {
            listOfTimes.clear()
        }
    }

    private fun headSetPlugging(intExtra: Int) {
        if (intExtra == HEAD_SET_PLUGGED) {
            Log.d(TAG, "headSetPlugging: HEAD_SET_PLUGGED")
        } else if (intExtra == HEAD_SET_UN_PLUGED) {

            musicNotificationReceiver.pausing()
        }

    }

}