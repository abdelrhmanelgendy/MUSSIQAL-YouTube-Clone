package com.example.musiqal.lyrics.lyricsdatabase.local.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.musiqal.database.remote.YoutubeApi
import com.example.musiqal.di.ApplicationModule
import com.example.musiqal.lyrics.lyricsdatabase.local.LyricsDatabase
import com.example.musiqal.lyrics.lyricsdatabase.local.LyricsDatabaseDao
import com.example.musiqal.lyrics.lyricsdatabase.local.mvi.LyricsDataBaseMainRepository
import com.example.musiqal.lyrics.mvi.LyricsMainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LyricsDiAppModule {
    private val LyricsDatabaseName = "lyrics_database"

    @Provides
    @Singleton
    fun provideLocalLyricsDao(application: Application): LyricsDatabaseDao {
        return Room.databaseBuilder(
            application.applicationContext, LyricsDatabase::class.java,
            LyricsDatabaseName
        ).build().getLocalLyricsDao()
    }

    @Provides
    @Singleton
    fun provideLocalLyricsRepository(dao: LyricsDatabaseDao): LyricsDataBaseMainRepository {
      return LyricsDataBaseMainRepository(dao)
    }

}