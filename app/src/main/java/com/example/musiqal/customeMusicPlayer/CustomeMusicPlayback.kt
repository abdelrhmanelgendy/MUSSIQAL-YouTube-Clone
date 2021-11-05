package com.example.musiqal.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.musiqal.R
import com.example.musiqal.customeMusicPlayer.*
import com.example.musiqal.customeMusicPlayer.musicNotification.NotificationHelper
import com.example.musiqal.customeMusicPlayer.musicNotification.NotificationListener
import com.example.musiqal.customeMusicPlayer.musicNotification.NotificationService
import com.example.musiqal.customeMusicPlayer.util.MusicPlayerEvents
import com.example.musiqal.models.youtubeItemInList.Item
import com.example.musiqal.ui.MainActivity
import kotlinx.coroutines.*
import java.lang.Runnable

class CustomeMusicPlayback(
    private val context: Context
) : MusicPlayerEvents, NotificationListener {
    var musicPlayerPersistence: MusicPlayerPersistence

    lateinit var onCustomeMusicPlayerCompletionListener: OnCustomeMusicPlayerCompletionListener

    companion object {
        lateinit var currentPlayListName: String
        lateinit var mediaPlayer: MediaPlayer
        lateinit var songPlayList: List<Item>
        lateinit var songItem: Item
        var songItemPosition: Int = -1
        var songItemDuration: Int = -1
        var _mediaPlayerPausedTimeInMillis = 0
        var tottalSongDuration = -1
    }


    lateinit var mediaPlayerItemTVTxtPlayedTimeInMinutes: TextView
    lateinit var mediaPlayerItemTVTxtRemainingTimeInMinutes: TextView
    lateinit var mediaPlayerItemMediaPlayerSeekBar: SeekBar
    lateinit var playPauseButtom: ImageView

    fun setViews(
        mediaPlayerItemTVTxtPlayedTimeInMinutes: TextView,
        mediaPlayerItemTVTxtRemainingTimeInMinutes: TextView,
        mediaPlayerItemMediaPlayerSeekBar: SeekBar,
        playPauseButtom: ImageView
    ) {
        this.mediaPlayerItemTVTxtPlayedTimeInMinutes = mediaPlayerItemTVTxtPlayedTimeInMinutes
        this.mediaPlayerItemTVTxtRemainingTimeInMinutes = mediaPlayerItemTVTxtRemainingTimeInMinutes
        this.mediaPlayerItemMediaPlayerSeekBar = mediaPlayerItemMediaPlayerSeekBar
        this.playPauseButtom = playPauseButtom
    }


    var isMediaPlayerPlaying = false
    val mainHandler = Handler(Looper.getMainLooper())
    private val TAG = "CustomeMusicPlayer"


    val EMPTY_TIME = "-:--"
    lateinit var notificationHelper: NotificationHelper
    var serviceIntent: Intent

    init {

        serviceIntent = Intent(context, NotificationService::class.java)
        musicPlayerPersistence = MusicPlayerPersistence(context)
        mediaPlayer = MediaPlayer()
        notificationHelper = NotificationHelper(context, this)

    }


    override fun start(
        url: String,
        currentItem: Item,
        currentPlayList: List<Item>,
        currentItemPosition: Int,
        playListName: String
    ) {

        CoroutineScope(Dispatchers.Main).launch {
            try {
                createCustomeNotification(
                    0,
                    playListName,
                    false
                )
            } catch (e: java.lang.Exception) {
                Log.d(TAG, "start: service " + e.message)
            }

        }

        isMediaPlayerPlaying = true
        currentPlayListName = playListName

        notificationHelper.createBasicNotification()
        MainActivity.isPlaying = true
        notificationHelper.isPlaying = true


        songPlayList = currentPlayList
        songItemPosition = currentItemPosition
        setPlayPauseButtonBackground(R.drawable.ic_baseline_play_arrow_24)


        emptyViews()
        CoroutineScope(Dispatchers.Unconfined).launch {
            try {
                startPlayingSong(url, songItem, playListName)
            } catch (e: java.lang.Exception) {
                Log.d(TAG, "start: " + e.message)
            }

        }
    }

    private fun startPlayingSong(url: String, item: Item, playListName: String) {

        CoroutineScope(Dispatchers.Default)
            .launch {
                try {

//                    val url=getAudioMP3EdURL(item)

                    mediaPlayer.stop()
                    mediaPlayer.release()
                    mediaPlayer =
                        MediaPlayer.create(context, MusicPlayerUtil.convertVideoUrlToUri(url))

                    mediaPlayer.start()
                    setPlayPauseButtonBackground(R.drawable.ic_baseline_pause_24)
                    setUpViews(item, playListName)

                } catch (e: java.lang.Exception) {
                    Log.d(TAG, "start: " + e.message)
                }
            }

    }


    private fun createCustomeNotification(
        tottalSongDuration: Long,
        playListName: String,
        isCurrentlyPlaying: Boolean
    ) {

        setNotificationMediaStyleImagesAndDurations(
            tottalSongDuration,
            playListName,
            isCurrentlyPlaying
        )
    }

    private fun setNotificationMediaStyleImagesAndDurations(
        tottalSongDuration: Long,
        playListName: String,
        isCurrentlyPlaying: Boolean
    ) {
        Log.d(TAG, "setNotificationMediaStyleImagesAndDurations: "+ songItem.snippet.title)
        val maxResolutionImageUrl = ImageUrlUtil.getMaxResolutionImageUrl(songItem)
        Glide.with(context)
            .asBitmap()
            .load(maxResolutionImageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    notificationHelper.setMediStyle(
                        resource,
                        tottalSongDuration,
                        songItem.snippet.title,
                        playListName
                    )
                    val buildNotification = notificationHelper.buildNotification()
                    NotificationService.customeNotification = buildNotification
                    startNotificationService()

                    if (isCurrentlyPlaying) {
                        startPlayingMediaService()
                    } else {
                        startPausingMediaService()
                    }


                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })


    }

    private fun startNotificationService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }

    private fun stopNotificationService() {
        CoroutineScope(Dispatchers.Main).launch {
            context.stopService(Intent(context, NotificationService::class.java))
        }


    }


    private fun mediaPlayerCompleted(mp: MediaPlayer?) {
        setPlayPauseButtonBackground(R.drawable.ic_baseline_play_arrow_24)
        onCustomeMusicPlayerCompletionListener.onComplete(mp!!)
        notificationHelper.setPausingNotificationActions(notificationHelper.notificationBuilder)
        NotificationService.customeNotification = notificationHelper.buildNotification()
        startNotificationService()
    }


    override fun pause() {


        CoroutineScope(Dispatchers.Default).launch {

            try {


                if (mediaPlayer.isPlaying) {
                    isMediaPlayerPlaying = false
                    mediaPlayer.pause()
                    setPlayPauseButtonBackground(R.drawable.ic_baseline_play_arrow_24)
                    _mediaPlayerPausedTimeInMillis = mediaPlayer.currentPosition
                    Log.d(TAG, "pause: " + _mediaPlayerPausedTimeInMillis)
                    startPausingMediaService()
                }
            } catch (e: Exception) {
                throw Exception(e.message)
            }

        }
    }


    override fun resume() {
        Log.d(TAG, "resume: " + mediaPlayer.hashCode())
        try {
            CoroutineScope(Dispatchers.Default).launch {
                if (!mediaPlayer.isPlaying) {
                        isMediaPlayerPlaying = true
                        setPlayPauseButtonBackground(R.drawable.ic_baseline_pause_24)
                        mediaPlayer.seekTo(_mediaPlayerPausedTimeInMillis)
                        mediaPlayer.start()
                        Log.d(TAG, "resume: " + _mediaPlayerPausedTimeInMillis)
                        startPlayingMediaService()
                    }



            }
        }catch (e:java.lang.Exception){
            isMediaPlayerPlaying = false
        }
    }

    override fun stop() {
        CoroutineScope(Dispatchers.Default).launch {
            mediaPlayer.pause()
            notificationHelper.pauseingMediaSession(mediaPlayer.currentPosition.toLong(), songItem)
            notificationHelper.isPlaying = true
            MainActivity.isPlaying = false
            isMediaPlayerPlaying = false
            saveCurrentPlayingMusicTime()
            stopNotificationService()


        }
        CoroutineScope(Dispatchers.Main).launch {
            setPlayPauseButtonBackground(R.drawable.ic_baseline_play_arrow_24)

        }
    }

    private fun saveCurrentPlayingMusicTime() {
        val trackId = songItem.snippet.resourceId.videoId
        val trackLastDuration = _mediaPlayerPausedTimeInMillis
        musicPlayerPersistence.saveLastPlayedDuration(trackLastDuration.toLong(), trackId)
    }


    override fun next(url: String) {
        Log.d(TAG, "next: " + url + " " + mediaPlayer.hashCode())
        CoroutineScope(Dispatchers.Default).launch {


        }
    }

    override fun previouse(url: String) {
        Log.d(TAG, "next: " + url + " " + mediaPlayer.hashCode())
        CoroutineScope(Dispatchers.Default).launch {


        }
    }


    override fun seekTo(newTime: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                mediaPlayer.seekTo(newTime)
                Log.d(TAG, "seekTo: " + newTime)
            } catch (e: Exception) {
                Log.d(TAG, "seekTo: " + e.message)
            }

        }
    }


    override fun addFavorite() {

    }


    fun emptyViews() {
        CoroutineScope(Dispatchers.Default).launch {
            withContext(Dispatchers.Main)
            {
                mediaPlayerItemMediaPlayerSeekBar.max = 0
                mediaPlayerItemMediaPlayerSeekBar.progress = 0
                mediaPlayerItemTVTxtPlayedTimeInMinutes.text = EMPTY_TIME
                mediaPlayerItemTVTxtRemainingTimeInMinutes.text = EMPTY_TIME
            }

        }


    }

    fun setUpViews(item: Item, playListName: String) {
        tottalSongDuration = mediaPlayer.duration

        CoroutineScope(Dispatchers.Main).launch {
            try {
                createCustomeNotification(
                    mediaPlayer.duration.toLong(),
                    playListName,
                    true
                )
            } catch (e: java.lang.Exception) {
                Log.d(TAG, "start: service " + e.message)
            }

        }


        isMediaPlayerPlaying = true
        updateViewsEverySecond(mediaPlayer)
        setSeekBarAndTextViewsData()
    }

    private fun setSeekBarAndTextViewsData() {
        mediaPlayer?.let {

            val tottalTimeInSec = it.duration / 1000
            val tottalTimeFormated = MusicPlayerUtil.formatTime(tottalTimeInSec)
            it.setOnCompletionListener {
                mediaPlayerCompleted(it)
            }
            Log.d(TAG, "setUpViews: " + tottalTimeFormated)
            CoroutineScope(Dispatchers.Default).launch {
                withContext(Dispatchers.Main)
                {
                    mediaPlayerItemTVTxtRemainingTimeInMinutes.text = tottalTimeFormated
                    mediaPlayerItemMediaPlayerSeekBar.also {
                        it.max = tottalTimeInSec
                        it.setOnSeekBarChangeListener(object :
                            SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(
                                seekBar: SeekBar?,
                                progress: Int,
                                fromUser: Boolean
                            ) {
                                if (fromUser) {

                                    seekTo(progress * 1000)
                                    onCustomeMusicPlayerCompletionListener.onSeeking(progress * 1000)
                                    _mediaPlayerPausedTimeInMillis = progress * 1000
                                    notificationHelper.seekingMediaSession((progress * 1000).toLong())
                                }
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {

                            }

                            override fun onStopTrackingTouch(seekBar: SeekBar?) {

                            }
                        })
                    }
                }
            }
        }
    }


    private fun updateViewsEverySecond(mediaPlayer: MediaPlayer?) {
        mainHandler.post(object : Runnable {
            override fun run() {
                Log.d(TAG, "run: " + mediaPlayer.hashCode())
                if (mediaPlayer != null) {

                    try {
                        updateSeekBarEverySecond(
                            mediaPlayer.currentPosition,
                            mediaPlayer.duration
                        )
                    } catch (e: Exception) {
                        Log.d(TAG, "run: " + e.message)
                    }
                }
                mainHandler.postDelayed(this, 100)
            }
        })

    }

    private fun updateSeekBarEverySecond(currentPosition: Int?, duration: Int?) {
        if (isMediaPlayerPlaying) {
            _mediaPlayerPausedTimeInMillis = currentPosition!!
            Log.d(
                TAG,
                "updateSeekBarEverySecond: " + (currentPosition!! / 1000) + " " + (duration!! / 1000)
            )
            val timeInSecond = currentPosition / 1000
            songItemDuration = currentPosition
            mediaPlayerItemMediaPlayerSeekBar.progress = timeInSecond
            mediaPlayerItemTVTxtPlayedTimeInMinutes.text = MusicPlayerUtil.formatTime(timeInSecond)


        }

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setPlayPauseButtonBackground(icBaselinePause24: Int) {
        playPauseButtom.setImageDrawable(
            context.resources.getDrawable(icBaselinePause24, context.theme)
        )

    }

    override fun seeking(currentPosition: Int) {
        Log.d(TAG, "seeking: " + currentPosition)
        seekTo(currentPosition)
    }

    fun startWithSeeking(
        url: String,
        currentItem: Item,
        currentPlayList: List<Item>,
        currentItemPosion: Int,
        playListName: String,
        currentDuration: Long
    ) {


        currentPlayListName = playListName
        notificationHelper = NotificationHelper(context, this)
        notificationHelper.createBasicNotification()
        MainActivity.isPlaying = true
        notificationHelper.isPlaying = true
        songPlayList = currentPlayList
        songItemPosition = currentItemPosion

        startPausingMediaService()
        setPlayPauseButtonBackground(R.drawable.ic_baseline_play_arrow_24)

        emptyViews()
        CoroutineScope(Dispatchers.Unconfined).launch {
            try {
                startPlayingSongWithSeeking(url, songItem, playListName, currentDuration)
            } catch (e: java.lang.Exception) {
                Log.d(TAG, "start: " + e.message)
            }

        }

    }

    private fun startPlayingSongWithSeeking(
        url: String,
        item: Item,
        playListName: String,
        currentDuration: Long
    ) {

        CoroutineScope(Dispatchers.Default)
            .launch {
                try {
                    isMediaPlayerPlaying = false
                    mediaPlayer.stop()
                    mediaPlayer.release()
                    mediaPlayer =
                        MediaPlayer.create(context, MusicPlayerUtil.convertVideoUrlToUri(url))
                    mediaPlayer.seekTo(currentDuration.toInt())

                    setPlayPauseButtonBackground(R.drawable.ic_baseline_play_arrow_24)
                    setUpViews(item, playListName)
                    startPlayingMediaService()

                } catch (e: java.lang.Exception) {
                    Log.d(TAG, "start: " + e.message)
                }
            }

    }

    fun startPlayingMediaService() {
        notificationHelper.setPlayingNotificationActions(notificationHelper.notificationBuilder)
        notificationHelper.playingMediaSession(_mediaPlayerPausedTimeInMillis.toLong())
        NotificationService.customeNotification = notificationHelper.buildNotification()
        notificationHelper.isPlaying = true
        MainActivity.isPlaying = true
        startNotificationService()
    }

    fun startPausingMediaService() {
        notificationHelper.setPausingNotificationActions(notificationHelper.notificationBuilder)
        notificationHelper.pauseingMediaSession(_mediaPlayerPausedTimeInMillis.toLong(), songItem)
        NotificationService.customeNotification = notificationHelper.buildNotification()
        notificationHelper.isPlaying = false
        MainActivity.isPlaying = false
        startNotificationService()
    }


}
