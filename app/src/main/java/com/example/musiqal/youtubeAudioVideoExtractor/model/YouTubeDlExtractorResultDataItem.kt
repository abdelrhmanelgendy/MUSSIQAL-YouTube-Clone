package com.example.musiqal.youtubeAudioVideoExtractor.model

import com.example.musiqal.youtubeAudioVideoExtractor.util.YoutubeDlResultFormatIDs

data class YouTubeDlExtractorResultDataItem(
    var abr: Double?,
    var acodec: String?,
    var asr: Int?,
    var container: String?,
    var downloader_options: DownloaderOptions?,
    var ext: String?,
    var filesize: Long?,
    var format: String?,
    var format_id: String?,
    var format_note: String?,
    var fps: Int?,
    var height: Int?,
    var http_headers: HttpHeaders?,
    var protocol: String?,
    var quality: Int?,
    var tbr: Double?,
    var url: String?,
    var vbr: Double?,
    var vcodec: String?,
    var width: Int?,
    var isItVideo: String=YoutubeDlResultFormatIDs.IsVideoState.UNKNOWN.msg
)