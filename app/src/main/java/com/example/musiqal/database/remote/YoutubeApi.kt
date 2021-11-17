package com.example.musiqal.database.remote

import com.example.musiqal.datamodels.youtubeApiChannel.YoutubeCategoryRequest
import com.example.musiqal.datamodels.youtubeApiSearchForPlayList.YoutubeApiSearchForPlayListRequest
import com.example.musiqal.datamodels.youtubeItemInList.YoutubeVideosInPlaylistRequest
import com.example.musiqal.datamodels.youtubeVideoDuaration.YouTubeVideoDurationResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {
//    https://youtube.googleapis.com/youtube/v3/search?part=
//     snippet&key=AIzaSyB8RbcQZ9mduHObgVtgGyPhDCfMmlQuwzQ&type=
//     video&q=assassins%20creed&tottalResults=500&videoCategoryId=15

    //https://www.googleapis.com/youtube/v3/videoCategories?
    // part=snippet&regionCode=EG&key=AIzaSyB8RbcQZ9mduHObgVtgGyPhDCfMmlQuwzQ

    //playList
    //https://youtube.googleapis.com/youtube/v3/search?part=snippet&key=AIzaSyB8RbcQZ9mduHObgVtgGyPhDCfMmlQuwzQ
    // &type=playlist&q=bestremix&maxResults=2&categoryId=15


    @GET("search")
    suspend fun searchForPlayLists(
        @Query("part") part: String,
        @Query("type") type: String,
        @Query("q") searchQuery: String,
        @Query("maxResults") maxResult: String,
        @Query("categoryId") videoCategoryId: String,
        @Query("key") api_key: String
    ): Response<YoutubeApiSearchForPlayListRequest>

    //https://youtube.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=
    // PLyEeuQ-a8E4Loc4zE57A6ARMS_cW8AukT&key=
    // AIzaSyB8RbcQZ9mduHObgVtgGyPhDCfMmlQuwzQ&maxResults=1
    @GET("playlistItems")
    suspend fun getVideosInPlayList(
        @Query("part") part: String,
        @Query("playlistId") playListId: String,
        @Query("maxResults") maxResult: String,
        @Query("key") api_key: String
    ): Response<YoutubeVideosInPlaylistRequest>

    @GET("videoCategories")
    suspend fun getYoutubeCategoryId(
        @Query("part") videoPart: String,
        @Query("regionCode") regionCode: String,
        @Query("key") apiKey: String
    ): Response<YoutubeCategoryRequest>


    //https://www.googleapis.com/youtube/v3/videos?part=contentDetails&id=rtOvBOTyX00&key=AIzaSyATskn_B0gM6QL7jEj2SeaxOBYfl4_GUIg

    @GET("videos")
    suspend fun getYoutubeVideoDuration(
        @Query("part") videoPart: String,
        @Query("id") videosId: List<String>,
        @Query("key") apiKey: String
    ): Response<YouTubeVideoDurationResult>

    //https://clients1.google.com/complete/search?client=youtube&hl=en&gl=sg&gs_rn=64&gs_ri=youtube&tok=h3yTGb1h3-yuCBwsAaQpxQ&ds=yt&cp=3&gs_id=2u&q=fifty%shades&callback=google.sbox.p50&gs_gbg=0l0MjG05RWnWBe9WcipQbsy




}