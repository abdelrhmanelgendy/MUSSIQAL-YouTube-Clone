package com.example.musiqal.search.searchDi

import android.app.Application
import androidx.room.Room
import com.example.musiqal.database.local.HistoryOfSearchingDao
import com.example.musiqal.database.local.PlaylistsDatabase

import com.example.musiqal.search.database.remot.SearchApi
import com.example.musiqal.search.mvi.SearchMainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchApplicationModule {
    private val youtubeAPIBaseUrl = "https://youtube.googleapis.com/youtube/v3/"

    @Provides
    @Singleton
    fun provideSearchHistoryDao(application: Application): HistoryOfSearchingDao {
        return Room.databaseBuilder(
            application,
            PlaylistsDatabase::class.java,
            "RandomPlayListDatabase"
        )
            .build()
            .getHistoryOfSearchingDao()
    }

    @Provides
    @Singleton
    fun provideYoutubeApi() = Retrofit.Builder()
        .baseUrl(youtubeAPIBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(SearchApi::class.java)

    @Provides
    @Singleton
    fun provideDefaultRepository(
        historyOfSearchingDao:
        HistoryOfSearchingDao,
        youtubeApi: SearchApi
    ): SearchMainRepository {
        return SearchMainRepository(historyOfSearchingDao,youtubeApi)
    }

}