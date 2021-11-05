package com.example.musiqal.models.youtube.converter.toaudio

data class YoutubeMp3ConverterData(
    val duration: Double,
    val link: String,
    val msg: String,
    val progress: Int,
    val status: String,
    val title: String
)