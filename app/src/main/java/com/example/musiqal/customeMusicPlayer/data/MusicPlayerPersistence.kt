package com.example.musiqal.customeMusicPlayer.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.musiqal.customeMusicPlayer.musicNotification.model.LastPlayedSongData
import com.example.musiqal.customeMusicPlayer.musicNotification.model.LastPlayedTrack
import com.example.musiqal.customeMusicPlayer.musicNotification.model.LastPlayedTrackConverter
import com.example.musiqal.customeMusicPlayer.musicNotification.model.LastPlayerSongDataConverter
import com.example.musiqal.customeMusicPlayer.util.RepeateMode
import com.example.musiqal.customeMusicPlayer.util.ShuffleMode
import com.example.musiqal.datamodels.youtubeItemInList.Item
import com.example.musiqal.datamodels.youtubeItemInList.YoutubeItemsConverters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MusicPlayerPersistence(private val context: Context) {


    companion object {
        val MUSIC_PLAYER_SETTINGS_FILE_NAME = "music_player_settings"
        val MUSIC_PLAYER_LAST_PLAYED_DATA = "last_data"
        val MUSIC_PLAYER_LAST_PLAYED_TRACK = "last_played_track"
        val MUSIC_PLAYER_REPEAT_MODE = "repeat_mode"
        val MUSIC_PLAYER_SHUFFLE_MODE = "shuffle_mode"
        val MUSIC_PLAYER_PLAY_LIST = "play_list"
        val TAG = "musicPlayerPreference"
        val SHARED_PREFERENCE_MODE = Context.MODE_PRIVATE
    }


    private var sharedPreferences: SharedPreferences

    init {
        sharedPreferences =
            context.getSharedPreferences(MUSIC_PLAYER_SETTINGS_FILE_NAME, SHARED_PREFERENCE_MODE)
    }


    /**
     * save and get last played music url
     *save and get last played music name
     *save and get last played music thubnails
     */
    fun saveLastPlayedSong(lastPlayedSongData: LastPlayedSongData) {
        val lastPlayedSongToString =
            LastPlayerSongDataConverter.convertLastPlayedSongToString(lastPlayedSongData)
        CoroutineScope(Dispatchers.Default).launch {

            try {
                val sharedEditor = sharedPreferences.edit()
                sharedEditor.putString(MUSIC_PLAYER_LAST_PLAYED_DATA, lastPlayedSongToString)
                    .apply()
                Log.d(TAG, "saveLastPlayedSong: "+lastPlayedSongToString)
            } catch (e: Exception) {
                Log.d(TAG, "saveLastPlayedSong: " + e.message)
            }
        }
    }

    fun getLastPlayedSong(): LastPlayedSongData {

        try {
            val lastPlayedSong = sharedPreferences.getString(MUSIC_PLAYER_LAST_PLAYED_DATA, "-1")
            val convertToLastPlayedSong =
                LastPlayerSongDataConverter.convertToLastPlayedSong(lastPlayedSong!!)
            Log.d(TAG, "getLastPlayedSong: "+lastPlayedSong)
            return convertToLastPlayedSong
        } catch (e: Exception) {
            Log.d(TAG, "saveLastPlayedSong: " + e.message)
            return LastPlayedSongData.provideEmptyPlayedSongData()
        }
    }


    /**
     *save and get repeate mode
     */
    fun saveRepeatMode(repeateMode: RepeateMode) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val sharedEditor = sharedPreferences.edit()
                sharedEditor.putString(MUSIC_PLAYER_REPEAT_MODE, repeateMode.repeatMode)
                    .apply()
                Log.d(TAG, "saveRepeatMode: "+repeateMode.repeatMode)
            } catch (e: Exception) {
                Log.d(TAG, "saveLastPlayedSong: " + e.message)
            }
        }
    }

    fun getRepeatMode(): RepeateMode {
        try {

            val repeateModeMgs =
                sharedPreferences.getString(MUSIC_PLAYER_REPEAT_MODE, "repeate_all")
            when (repeateModeMgs) {
                "no_repeat" -> return RepeateMode.NoRepeating
                "repeate_once" -> return RepeateMode.RepeateOnc
                "repeate_all" -> return RepeateMode.RepeateAll
            }
            return RepeateMode.RepeateAll
        } catch (e: Exception) {
            Log.d(TAG, "saveLastPlayedSong: " + e.message)
            return RepeateMode.RepeateAll

        }


    }


    /**
     *save and get last played song duration
     */
    fun saveLastPlayedDuration(playedDuration: Long,trackId:String) {

        val lastTrack= LastPlayedTrack(playedDuration,trackId)
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val sharedEditor = sharedPreferences.edit()
                sharedEditor.putString(MUSIC_PLAYER_LAST_PLAYED_TRACK, LastPlayedTrackConverter().convertToStringTrack(lastTrack))
                    .apply()
                Log.d(TAG, "saveLastPlayedDuration: "+playedDuration)
            } catch (e: Exception) {
                Log.d(TAG, "saveLastPlayedSong: " + e.message)
            }
        }
    }

    fun getLastPlayedTrack(): LastPlayedTrack {
        try {

            val lastPlayedTrack =
                sharedPreferences.getString(MUSIC_PLAYER_LAST_PLAYED_TRACK,"-1")

            return LastPlayedTrackConverter().convertToPlayedTrack(lastPlayedTrack!!)
        } catch (e: Exception) {
            Log.d(TAG, "saveLastPlayedSong: " + e.message)
            return LastPlayedTrack.dummyTrack()

        }


    }


    /**
     *save and get shuffle mode
     */
    fun saveShuffleMode(shuffleMode: ShuffleMode) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val sharedEditor = sharedPreferences.edit()
                sharedEditor.putString(MUSIC_PLAYER_SHUFFLE_MODE, shuffleMode.shuffleMode)
                    .apply()
                Log.d(TAG, "saveShuffleMode: ")
            } catch (e: Exception) {
                Log.d(TAG, "saveLastPlayedSong: " + e.message)
            }
        }
    }

    fun getShuffleMode(): ShuffleMode {
        try {

            val shuffleMode =
                sharedPreferences.getString(MUSIC_PLAYER_SHUFFLE_MODE, "No-Shuffle")
            when (shuffleMode) {
                "No-Shuffle" -> return ShuffleMode.NoShuffle
                "Shuffle" -> return ShuffleMode.Shuffle

            }
            return ShuffleMode.NoShuffle
        } catch (e: Exception) {
            Log.d(TAG, "saveLastPlayedSong: " + e.message)
            return ShuffleMode.NoShuffle

        }


    }


    fun saveLastPlayedList(items:List<Item>) {
        val listOfItemsToSave =
            YoutubeItemsConverters().convertToString(items)
        CoroutineScope(Dispatchers.Default).launch {
            kotlin.runCatching {
                val sharedEditor = sharedPreferences.edit()
                sharedEditor.putString(MUSIC_PLAYER_PLAY_LIST, listOfItemsToSave)
                    .apply()
                Log.d(TAG, "saveLastPlayedList: ")
            }.onFailure {
                Log.d(TAG, "savedLastPlayList: " + it.message)
            }

        }
    }

    fun getLastPlayedList(): List<Item> {

        try {
            val lastPlayedSong = sharedPreferences.getString(MUSIC_PLAYER_PLAY_LIST, "-1")
            val convertToListOfItems =
                YoutubeItemsConverters().convertItems(lastPlayedSong!!)
            Log.d(TAG, "getLastPlayedList: "+lastPlayedSong)
            return convertToListOfItems
        } catch (e: Exception) {
            Log.d(TAG, "saveLastPlayedSong: " + e.message)
            return listOf()
        }
    }



}