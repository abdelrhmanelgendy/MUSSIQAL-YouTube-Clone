package com.example.musiqal.lyrics.lyricsdatabase.local.mvi

import android.util.Log
import com.example.musiqal.lyrics.lyricsdatabase.local.LyricsDatabaseDao
import com.example.musiqal.lyrics.model.LyricsLocalDataModel
import com.example.musiqal.util.Resource
import javax.inject.Inject

class LyricsDataBaseMainRepository @Inject constructor(private val lyricsDatabaseDao: LyricsDatabaseDao) :
    LyricsDataBaseDefaultRepository {

    override suspend fun insertLyricsData(lyricsLocalDataModel: LyricsLocalDataModel) {
        try {
            Log.d("TAG", "adding02: ")

            lyricsDatabaseDao.insertNewTrackLyrics(lyricsLocalDataModel.songName,lyricsLocalDataModel.songName,lyricsLocalDataModel.songLyrics,lyricsLocalDataModel.songDuration,lyricsLocalDataModel.videoId)
        } catch (e: java.lang.Exception) {
            Log.d("TAG", "getAllLyricsData: " + e.message)

        }
    }

    override suspend fun getAllLyricsData(): Resource<List<LyricsLocalDataModel>> {

//        try {
//            val allTracksLyrics = lyricsDatabaseDao.getAllTracksLyrics()
//            return Resource.Success(allTracksLyrics)
//        } catch (e: Exception) {
//            Log.d("TAG", "getAllLyricsData: " + e.message)
            return Resource.Failed("e.message!!")
//        }

    }

    override suspend fun deleteLyricsData(trackId: Int) {
//        lyricsDatabaseDao.deleteTrackLyrics(trackId)
    }

}