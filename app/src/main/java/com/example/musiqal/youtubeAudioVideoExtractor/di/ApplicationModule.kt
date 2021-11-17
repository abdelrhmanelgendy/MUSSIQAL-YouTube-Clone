package com.example.musiqal.youtubeAudioVideoExtractor.di

import android.app.Application
import androidx.room.Room
import com.example.musiqal.userPLaylists.database.PlaylistsDatabase
import com.example.musiqal.youtubeAudioVideoExtractor.database.local.YoutubeExtractedFileDao
import com.example.musiqal.youtubeAudioVideoExtractor.database.remote.YoutubeExtractorApi
import com.example.musiqal.youtubeAudioVideoExtractor.mvi.YoutubeExtractorMainRepository
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
    val baseUrl="http://abdelrhman.pythonanywhere.com/"

    @Provides
    @Singleton
    fun provideYoutubeAPi()=
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YoutubeExtractorApi::class.java)

    @Provides
    @Singleton
    fun provideExtractionDao(application: Application) =
        Room.databaseBuilder(
            application.applicationContext,
            PlaylistsDatabase::class.java,
            "playLists_database"
        )
            .build()
            .getExtractionDao()

    @Provides
    @Singleton
    fun provideYoutubeMainRepository(youtubeExtractorApi: YoutubeExtractorApi,youtubeExtractedFileDao: YoutubeExtractedFileDao)
    =YoutubeExtractorMainRepository(youtubeExtractorApi,youtubeExtractedFileDao)




}