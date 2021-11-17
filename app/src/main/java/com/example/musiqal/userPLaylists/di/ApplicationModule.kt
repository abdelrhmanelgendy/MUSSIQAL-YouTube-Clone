package com.example.musiqal.userPLaylists.di

import android.app.Application
import androidx.room.Room
import com.example.musiqal.userPLaylists.database.PlaylistsDatabase
import com.example.musiqal.userPLaylists.database.UserPlaylistDao
import com.example.musiqal.userPLaylists.mvi.UserPlayListsMainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApplicationModule {

    @Provides
    @Singleton
    fun providePLaylistDao(application: Application) =
        Room.databaseBuilder(
            application.applicationContext,
            PlaylistsDatabase::class.java,
            "playLists_database"
        )
            .build()
            .getUserPLayListDao()




    @Provides
    @Singleton
    fun providsPlaylistsRepository(userPlaylistDao: UserPlaylistDao) =
        UserPlayListsMainRepository(userPlaylistDao)


}