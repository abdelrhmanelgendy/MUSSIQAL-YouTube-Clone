package com.example.musiqal.repository

import com.example.musiqal.database.local.HistoryOfPlayedItemDao
import com.example.musiqal.database.local.RandomPlaylistsDao
import com.example.musiqal.database.remote.YoutubeApi
import com.example.musiqal.database.remote.YoutubeToMp3ApiConverter
import com.example.musiqal.datamodels.youtube.converter.toaudio.YoutubeMp3ConverterData
import com.example.musiqal.datamodels.youtubeApiChannel.YoutubeCategoryRequest
import com.example.musiqal.datamodels.youtubeApiSearchForPlayList.YoutubeApiSearchForPlayListRequest
import com.example.musiqal.datamodels.youtubeItemInList.YoutubeVideosInPlaylistRequest
import com.example.musiqal.datamodels.youtubeVideoDuaration.YouTubeVideoDurationResult
import com.example.musiqal.util.CustomeMusicPlayback
import com.example.musiqal.util.Resource
import java.lang.Exception
import javax.inject.Inject

class YoutubeMainRepository @Inject constructor(
    val youtubeApi: YoutubeApi,
    val randomPlaylistsDao: RandomPlaylistsDao,
    val historyOfPlayedItemDao: HistoryOfPlayedItemDao,
    val youtubeToMp3ApiConverter: YoutubeToMp3ApiConverter,
    val customeMusicPlayback: CustomeMusicPlayback


) : YoutubeDefaultRepository {

    override suspend fun searchForPlayList(
        part: String,
        type: String,
        searchQuery: String,
        maxResult: String,
        categoryId: String,
        api_key: String
    ): Resource<YoutubeApiSearchForPlayListRequest> {
        return try {
            val searchForPlayList = youtubeApi.searchForPlayLists(
                part,
                type,
                searchQuery,
                maxResult,
                categoryId,
                api_key
            )
            if (searchForPlayList.isSuccessful) {
                return Resource.Success(searchForPlayList.body()!!)
            } else {
                return Resource.Failed(searchForPlayList.message().toString())
            }


        } catch (e: Exception) {
            Resource.Failed(e.message.toString())
        }
    }

    override suspend fun getVideosInPlayList(
        part: String,
        playListId: String,
        maxResult: String,
        api_key: String
    ): Resource<YoutubeVideosInPlaylistRequest> {
        return try {
            val playlistVideos = youtubeApi.getVideosInPlayList(
                part, playListId, maxResult, api_key
            )
            if (playlistVideos.isSuccessful) {
                return Resource.Success(playlistVideos.body()!!)
            } else {
                return Resource.Failed(playlistVideos.message().toString())
            }


        } catch (e: Exception) {
            Resource.Failed(e.message.toString())
        }
    }




    override suspend fun getYoutubeCategoryId(
        videoPart: String,
        regionCode: String,
        apiKey: String
    ): Resource<YoutubeCategoryRequest> {
        return try {
            val searchForCategory = youtubeApi.getYoutubeCategoryId(
                videoPart, regionCode, apiKey
            )
            if (searchForCategory.isSuccessful) {
                return Resource.Success(searchForCategory.body()!!)
            } else {
                return Resource.Failed(searchForCategory.message().toString())
            }


        } catch (e: Exception) {
            Resource.Failed(e.message.toString())
        }
    }

    override suspend fun getVideoMp3ConvertedUrl(
        rapidHost: String,
        rapidApiKey: String,
        videoId: String
    ): Resource<YoutubeMp3ConverterData> {
        return try {
            val getVideoUrl = youtubeToMp3ApiConverter.getMp3Url(rapidHost, rapidApiKey, videoId)
            if (getVideoUrl.isSuccessful) {
                Resource.Success(getVideoUrl.body()!!)
            } else {
                return Resource.Failed(getVideoUrl.message().toString())
            }
        } catch (e: Exception) {
            Resource.Failed(e.message.toString())
        }
    }


    override suspend fun deleteYoutubePlayList(id: Int) {
        randomPlaylistsDao.deleteItem(id)
    }

    override suspend fun insertYoutubePlayList(youtubeApiSearchForPlayListRequest: YoutubeApiSearchForPlayListRequest) {
        randomPlaylistsDao.insertPlayList(youtubeApiSearchForPlayListRequest)
    }

    override suspend fun selectSavedYoutubaPlayList(playListId: Int): Resource<YoutubeApiSearchForPlayListRequest> {
        return try {

            val items = randomPlaylistsDao.getItem(playListId)
            Resource.Success(items)

        } catch (e: Exception) {
            Resource.Failed(e.message.toString())
        }
    }

    override suspend fun insertPlayedTrack(item: com.example.musiqal.datamodels.youtubeItemInList.Item) {
        historyOfPlayedItemDao.insertTrack(item)
    }

    override suspend fun deletePlayedTrack(itemId: Int) {
        historyOfPlayedItemDao.deleteTrack(itemId)
    }

    override suspend fun deleteAllSavedPlayedTrack() {
        historyOfPlayedItemDao.deleteAllSavedTrack()
    }

    override suspend fun getVideoDuration(part: String, videosId: List<String>, apiKey: String): Resource<YouTubeVideoDurationResult> {
        return try {
            val getDuartion = youtubeApi.getYoutubeVideoDuration(part,videosId,apiKey)
            if (getDuartion.isSuccessful) {
                Resource.Success(getDuartion.body()!!)
            } else {
                return Resource.Failed(getDuartion.message().toString())
            }
        } catch (e: Exception) {
            Resource.Failed(e.message.toString())
        }
    }

    override suspend fun getAllPlayedTracks():Resource< List<com.example.musiqal.datamodels.youtubeItemInList.Item> >{
        return try {
            val items = historyOfPlayedItemDao.selectAllTracks()
            Resource.Success(items)
        } catch (e: Exception) {
            Resource.Failed(e.message.toString())
        }

    }

    fun provideCustomeMusicPlayback(): CustomeMusicPlayback {
        return customeMusicPlayback
    }

}