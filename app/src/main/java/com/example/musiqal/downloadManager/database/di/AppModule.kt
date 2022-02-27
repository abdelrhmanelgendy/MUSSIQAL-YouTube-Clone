package com.example.musiqal.downloadManager.database.di

import android.app.Application
import androidx.room.Room
import com.example.musiqal.downloadManager.database.DownloadableFilesDao
import com.example.musiqal.downloadManager.database.DownloadableFilesDataBase
import com.example.musiqal.downloadManager.database.DownloadableFilesDataBase.Companion.DownloadableFilesDataBaseName
import com.example.musiqal.downloadManager.mvi.repository.DownloadableFilesMainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideDownloadableFilesDao(application: Application): DownloadableFilesDao =
        Room.databaseBuilder(
            application.applicationContext,
            DownloadableFilesDataBase::class.java,
            DownloadableFilesDataBaseName
        )
            .build().getDownloadableFilesDao()

    @Provides
    @Singleton
    fun provideDownloadableFilesMainRepository(downloadableFilesDao: DownloadableFilesDao):
            DownloadableFilesMainRepository =
        DownloadableFilesMainRepository(downloadableFilesDao)


}