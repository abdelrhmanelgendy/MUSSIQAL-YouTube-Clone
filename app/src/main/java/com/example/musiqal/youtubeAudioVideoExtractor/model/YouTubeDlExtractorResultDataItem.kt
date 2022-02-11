package com.example.musiqal.youtubeAudioVideoExtractor.model

import com.example.musiqal.youtubeAudioVideoExtractor.util.YoutubeDlResultFormatIDs

data class YouTubeDlExtractorResultDataItem(
    var abr: Double? =0.0,
    var acodec: String?="",
    var asr: Int?=-1,
    var container: String?="",
    var downloader_options: DownloaderOptions?=null,
    var ext: String?=".mp3",
    var filesize: Long?=-1,
    var format: String?="Audio",
    var format_id: String?="137",
    var format_note: String?="Audio",
    var fps: Int?=60,
    var height: Int?=60,
    var http_headers: HttpHeaders?=null,
    var protocol: String?="https",
    var quality: Int?=360,
    var tbr: Double?=0.0,
    var url: String?,
    var vbr: Double?=0.0,
    var vcodec: String?="",
    var width: Int?=-1,
    var isItVideo: String = YoutubeDlResultFormatIDs.IsVideoState.UNKNOWN.msg,
    var videoTitle: String? = null,
    var videoQualityId: String? = null,
    var videoDuration: String? = null,
    var isSelected:Boolean=false,
    var isFileValide:Boolean?=true

)