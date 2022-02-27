package com.example.musiqal.downloadManager.data

sealed class DownLoadedState(val state:Int)
{
    object Success: DownLoadedState(1)
    object  Complete: DownLoadedState(2)
    object  Failed: DownLoadedState(-1)
    object  UnKnown: DownLoadedState(5)
    object  Started: DownLoadedState(0)
}