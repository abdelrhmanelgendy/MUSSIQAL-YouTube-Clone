package com.example.musiqal.customeMusicPlayer.musicNotification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.MediaMetadata
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.KeyEvent
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.musiqal.R
import com.example.musiqal.customeMusicPlayer.musicNotification.MusicPlayerActions.Companion.ACTION_CLOSE
import com.example.musiqal.customeMusicPlayer.musicNotification.MusicPlayerActions.Companion.ACTION_NEXT
import com.example.musiqal.customeMusicPlayer.musicNotification.MusicPlayerActions.Companion.ACTION_PLAY
import com.example.musiqal.customeMusicPlayer.musicNotification.MusicPlayerActions.Companion.ACTION_PREVIOUS
import com.example.musiqal.models.youtubeItemInList.Item
import com.example.musiqal.ui.MainActivity
import com.example.musiqal.util.BaseApplication
import com.example.musiqal.util.ImageUrlUtil

class NotificationHelper(
    private val context: Context,
    private val notificationListener: NotificationListener
) {
    private lateinit var notificationManager: NotificationManager
    var isPlaying = true
    lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var mediaSessionCompat: MediaSessionCompat
    init {
        mediaSessionCompat = MediaSessionCompat(context, "MUSIQAL")
    }
    fun createBasicNotification() {

        initIntents(context)

        notificationBuilder =
            NotificationCompat.Builder(context, BaseApplication.CHANNEL_PLAYING_MUSIC_ID)
                .setSmallIcon(R.drawable.musiqal_logo_large)
                .setContentIntent(getMainActivityContentIntent())
                .setOnlyAlertOnce(true)
                .setOngoing(false)
                .setAutoCancel(true)
                .setColor(Color.BLACK)
                .setAutoCancel(true)
                .setShowWhen(false)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)

    }

    lateinit var prevPendingIntent: PendingIntent
    lateinit var nextPendingIntent: PendingIntent
    lateinit var playPausePendingIntent: PendingIntent
    lateinit var closePendingIntent: PendingIntent

    private fun initIntents(context: Context) {
        val prevIntent =
            Intent(context, NotificationReciever::class.java).setAction(ACTION_PREVIOUS)
        prevPendingIntent =
            PendingIntent.getBroadcast(context, 51, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent = Intent(context, NotificationReciever::class.java).setAction(ACTION_NEXT)
        nextPendingIntent =
            PendingIntent.getBroadcast(context, 51, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val closeIntent = Intent(context, NotificationReciever::class.java).setAction(ACTION_CLOSE)
        closePendingIntent =
            PendingIntent.getBroadcast(context, 51, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT)


        val playPauseIntent =
            Intent(context, NotificationReciever::class.java).setAction(ACTION_PLAY)
        playPausePendingIntent =
            PendingIntent.getBroadcast(
                context,
                51,
                playPauseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

    }

    @SuppressLint("RestrictedApi")
    fun setPlayingNotificationActions(builder: NotificationCompat.Builder) {
        builder.mActions.clear()
        builder.addAction(R.drawable.ic_previous_18, "PREVIOUS", prevPendingIntent)
            .addAction(R.drawable.ic_baseline_pause_18, "PLAY", playPausePendingIntent)
            .addAction(R.drawable.ic_next_18, "NEXT", nextPendingIntent)
            .addAction(R.drawable.ic_baseline_close_18, "FINISH", closePendingIntent)
    }

    @SuppressLint("RestrictedApi")
    fun setPausingNotificationActions(builder: NotificationCompat.Builder) {
        builder.mActions.clear()
        builder
            .addAction(R.drawable.ic_previous_18, "PREVIOUS", prevPendingIntent)
            .addAction(R.drawable.ic_baseline_play_arrow_18, "PLAY", playPausePendingIntent)
            .addAction(R.drawable.ic_next_18, "NEXT", nextPendingIntent)
            .addAction(R.drawable.ic_baseline_close_18, "FINISH", closePendingIntent)

    }

    fun setMediStyle(bitmap: Bitmap, tottalSongDuration: Long, currentSongName: String, playListName: String) {

        Log.d("TAG", "setNotificationMediaStyleImagesAndDurations1")
        val mediaCompatSession =
            createMediaCompatSession(bitmap, tottalSongDuration, currentSongName,playListName)
        setPlayingNotificationActions(this.notificationBuilder)
        notificationBuilder.setStyle(
            androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1, 2,3)
                .setMediaSession(mediaCompatSession.sessionToken)
        )
    }

    private fun createMediaCompatSession(
        currentSongbitmap: Bitmap,
        tottalMediaDuration: Long,
        currentSongName: String, playListName: String
    ): MediaSessionCompat {
        Log.d("TAG", "setNotificationMediaStyleImagesAndDurations3: "+currentSongName)
        notificationBuilder.setContentTitle(playListName)
        notificationBuilder.setContentText(currentSongName)
            .setLargeIcon(currentSongbitmap)
        mediaSessionCompat = MediaSessionCompat(context, "MUSIQAL")
        mediaSessionCompat.isActive = true
        val mediaMetadata =
            MediaMetadata.Builder()
                .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, currentSongbitmap)

                .build()

        mediaSessionCompat.setMetadata(MediaMetadataCompat.fromMediaMetadata(mediaMetadata))


        //                .putString(
//                    MediaMetadata.METADATA_KEY_TITLE,
//                    "AudioPlayerService.activeBook.title"
//                )
//                .putString(
//                    MediaMetadata.METADATA_KEY_ART_URI,
//                    "AudioPlayerService.activeBook.title"
//                )
//                .putLong(
//                    MediaMetadata.METADATA_KEY_DURATION,
//                    tottalMediaDuration
//                )
//        mediaSessionCompat.setPlaybackState(
//            PlaybackStateCompat.Builder()
//                .setState(
//                    PlaybackStateCompat.STATE_PLAYING,
//                    0,
//                    1f
//                ).setActions(PlaybackStateCompat.ACTION_SEEK_TO)
//                .build()
//        )
////
//        mediaSessionCompat.setCallback(object : MediaSessionCompat.Callback() {
//            override fun onSeekTo(pos: Long) {
//                super.onSeekTo(pos)
//                if (isPlaying) {
//                    mediaSessionCompat.setPlaybackState(
//                        PlaybackStateCompat.Builder()
//                            .setState(
//                                PlaybackStateCompat.STATE_PLAYING,
//                                pos,
//                                1f
//                            )
//                            .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
//                            .build()
//                    )
//                } else {
//                    mediaSessionCompat.setPlaybackState(
//                        PlaybackStateCompat.Builder()
//                            .setState(
//                                PlaybackStateCompat.STATE_PAUSED,
//                                pos,
//                                1f
//                            )
//                            .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
//                            .build()
//                    )
//                }
//                notificationListener.seeking(pos.toInt())
//
//
//            }
//        })
        return mediaSessionCompat
    }

    fun pauseingMediaSession(currentPosition: Long,item:Item) {
//        mediaSessionCompat.setPlaybackState(
//            PlaybackStateCompat.Builder()
//                .setState(
//                    PlaybackStateCompat.STATE_PAUSED,
//                    currentPosition,
//                    1f
//                ).setActions(PlaybackStateCompat.ACTION_SEEK_TO)
//                .build()
//        )
//        Glide.with(context)
//            .asBitmap()
//            .load(ImageUrlUtil.getMaxResolutionImageUrl(item))
//            .into(object :CustomTarget<Bitmap>(){
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    val mediaMetadata =
//                        MediaMetadata.Builder()
//                            .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, currentSongbitmap)
//
//                            .build()
//                    mediaSessionCompat.setMetadata(MediaMetadataCompat.fromMediaMetadata(mediaMetadata))
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                }
//            })

    }

    fun playingMediaSession(currentPosition: Long) {
        mediaSessionCompat.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    currentPosition,
                    1f
                ).setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                .build()
        )
    }

    fun seekingMediaSession(currentPosition: Long) {
//        if (isPlaying) {
//            mediaSessionCompat.setPlaybackState(
//                PlaybackStateCompat.Builder()
//                    .setState(
//                        PlaybackStateCompat.STATE_PLAYING,
//                        currentPosition,
//                        1f
//                    )
//                    .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
//                    .build()
//            )
//        } else {
//            mediaSessionCompat.setPlaybackState(
//                PlaybackStateCompat.Builder()
//                    .setState(
//                        PlaybackStateCompat.STATE_PAUSED,
//                        currentPosition,
//                        1f
//                    )
//                    .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
//                    .build()
//            )
//        }
    }


    fun buildNotification(): Notification {
        return notificationBuilder.build()
    }

    fun getMainActivityContentIntent(): PendingIntent {
        val mainActivtyIntent = Intent(context, MainActivity::class.java)
        val mainActivityInt_REQ_CODE = 51
        return PendingIntent.getActivity(
            context,
            mainActivityInt_REQ_CODE,
            mainActivtyIntent,
            0
        )
    }

}