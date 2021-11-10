package com.example.musiqal.di

import android.app.Application
import androidx.room.Room
import com.android.volley.toolbox.Volley
import com.example.musiqal.database.local.HistoryOfPlayedItemDao
import com.example.musiqal.database.local.RandomPlaylistsDao
import com.example.musiqal.database.local.PlaylistsDatabase
import com.example.musiqal.database.remote.LyricsApiSource
import com.example.musiqal.database.remote.YoutubeApi
import com.example.musiqal.database.remote.YoutubeToMp3ApiConverter
import com.example.musiqal.helpers.CalenderHelper
import com.example.musiqal.lyrics.mvi.LyricsMainRepository

import com.example.musiqal.repository.YoutubeMainRepository
import com.example.musiqal.ui.DummyClass
import com.example.musiqal.userPLaylists.database.UserPlaylistDao
import com.example.musiqal.userPLaylists.mvi.UserPlayListsMainRepository
import com.example.musiqal.util.CustomeMusicPlayback
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    private val youtubeAPIBaseUrl = "https://youtube.googleapis.com/youtube/v3/"
    private val lyricsAPIBaseUrl = "https://api.lyrics.ovh/"
    private val liveCaptionBaseUrl = "https://video.google.com/"
    private val youtubeVideoToMp3ConverterUrl = "https://youtube-mp36.p.rapidapi.com/"

    @Provides
    @Singleton
    fun provideYoutubeApi() = Retrofit.Builder()
        .baseUrl(youtubeAPIBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(YoutubeApi::class.java)

    @Provides
    @Singleton
    fun provideLyricsApiSource() = Retrofit.Builder()
        .baseUrl(lyricsAPIBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(LyricsApiSource::class.java)

    @Provides
    @Singleton
    fun provideYoutubeToMp3ConverterApi() = Retrofit.Builder()
        .baseUrl(youtubeVideoToMp3ConverterUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build().create(YoutubeToMp3ApiConverter::class.java)


    @Provides
    @Singleton
    fun provideYoutubeRepository(
        youtubeApi: YoutubeApi,
        randomPlaylistsDao: RandomPlaylistsDao,
        historyOfPlayedItemDao: HistoryOfPlayedItemDao,
        youtubeToMp3ApiConverter: YoutubeToMp3ApiConverter,
        customeMusicPlayback: CustomeMusicPlayback,
    ) =
        YoutubeMainRepository(
            youtubeApi,
            randomPlaylistsDao,
            historyOfPlayedItemDao,
            youtubeToMp3ApiConverter,
            customeMusicPlayback
        )

    @Provides
    @Singleton
    fun provideLyricsRepository(
        lyricsApiSource: LyricsApiSource,
    ) =
        LyricsMainRepository(
            lyricsApiSource
        )


    @Provides
    @Singleton
    fun provideRandomPlayListDao(application: Application): RandomPlaylistsDao {
        return Room.databaseBuilder(
            application,
            PlaylistsDatabase::class.java,
            "RandomPlayListDatabase"
        ).build().getPlayListsDao()
    }


    @Provides
    @Singleton
    fun provideHistoryPlayListDao(application: Application): HistoryOfPlayedItemDao {
        return Room.databaseBuilder(
            application,
            PlaylistsDatabase::class.java,
            "RandomPlayListDatabase"
        ).build().gethistoryOfPlayedItemDao()
    }


    @Provides
    @Singleton
    fun provideSingleMusicPlayer(application: Application):
            CustomeMusicPlayback {
        return CustomeMusicPlayback(application)
    }

    @Provides
    @Singleton
    fun provideDummt(): DummyClass {
        return DummyClass()
    }


    @Provides
    @Singleton
    fun provideCalenderHelper():CalenderHelper{
        return CalenderHelper()
    }
//

//
//    @Provides
//    @Singleton
//    fun provideUserPLayListRepository(
//        userPlaylistDao: UserPlaylistDao
//    ) =
//        UserPlayListsMainRepository(userPlaylistDao)
//
//}

}