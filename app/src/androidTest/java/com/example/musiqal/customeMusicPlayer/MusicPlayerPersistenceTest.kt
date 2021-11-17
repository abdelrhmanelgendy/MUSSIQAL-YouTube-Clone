package com.example.musiqal.customeMusicPlayer

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.musiqal.customeMusicPlayer.data.MusicPlayerPersistence
import com.example.musiqal.customeMusicPlayer.musicNotification.model.LastPlayedSongData
import com.example.musiqal.customeMusicPlayer.util.RepeateMode
import com.example.musiqal.customeMusicPlayer.util.ShuffleMode
import com.example.musiqal.datamodels.youtubeItemInList.Item
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MusicPlayerPersistenceTest {


    private lateinit var musicPlayerPersistence: MusicPlayerPersistence

    @Before
    fun setUp() {

        musicPlayerPersistence = MusicPlayerPersistence(ApplicationProvider.getApplicationContext())
    }


    @ExperimentalCoroutinesApi
    @Test
    fun test_saving_last_playing_song_with_last_song_data_returns_true() {
        val lastPlayedSongDataDummy = LastPlayedSongData(LastPlayedSongData.item,50)
        var lastPlayedSong: LastPlayedSongData?=null
        runBlockingTest {
            musicPlayerPersistence.saveLastPlayedSong(lastPlayedSongDataDummy)
        }
        runBlockingTest {
            lastPlayedSong = musicPlayerPersistence.getLastPlayedSong()
        }
        assertThat(lastPlayedSong?.itemPosition).isEqualTo(50)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_saving_shuffle_mode_returns_true() {


        val shuffleMode = ShuffleMode.Shuffle
        var savedShuffle: ShuffleMode? = null
        runBlockingTest{
            musicPlayerPersistence.saveShuffleMode(shuffleMode)
        }
        runBlockingTest{
            savedShuffle = musicPlayerPersistence.getShuffleMode()
        }
        assertThat(savedShuffle?.shuffleMode).isEqualTo(shuffleMode.shuffleMode)

    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_saving_repeat_mode_returns_true() {


        val repeateMode = RepeateMode.RepeateOnc
        var savedRepeateMode: RepeateMode? = null
        runBlockingTest{
            musicPlayerPersistence.saveRepeatMode(repeateMode)
        }
        runBlockingTest{
            savedRepeateMode = musicPlayerPersistence.getRepeatMode()
        }
        assertThat(savedRepeateMode?.repeatMode).isEqualTo(repeateMode.repeatMode)

    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_saving_last_played_song_duration_with_negative_value_saving_zero_returns_true() {


//        val lastDuration = -1
//        var savedDuration: Int? = null
//        runBlockingTest{
//            musicPlayerPersistence.saveLastPlayedDuration(lastDuration)
//        }
//        runBlockingTest{
//            savedDuration = musicPlayerPersistence.getLastPlayedDuration()
//        }
//        assertThat(savedDuration).isEqualTo(0)

    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_saving_last_played_song_duration_with_positive_value_returns_true() {


//        val lastDuration = 5054512
//        var savedDuration: Int? = null
//        runBlockingTest{
//            musicPlayerPersistence.saveLastPlayedDuration(lastDuration)
//        }
//        runBlockingTest{
//            savedDuration = musicPlayerPersistence.getLastPlayedDuration()
//        }
//        assertThat(savedDuration).isEqualTo(lastDuration)

    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_saving_last_played_list_of_songs_returns_true() {


        val listOfSongs = listOf<Item>(LastPlayedSongData.item, LastPlayedSongData.item)
        var savedListOfSOngs: List<Item>? = null
        runBlockingTest{
            musicPlayerPersistence.saveLastPlayedList(listOfSongs)
        }
        runBlockingTest{
            savedListOfSOngs = musicPlayerPersistence.getLastPlayedList()
        }
        assertThat(savedListOfSOngs?.size).isEqualTo(listOfSongs.size)

    }



}