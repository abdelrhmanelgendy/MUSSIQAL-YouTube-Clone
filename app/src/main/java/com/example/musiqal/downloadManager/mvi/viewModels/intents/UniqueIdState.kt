package com.example.musiqal.downloadManager.mvi.viewModels.intents

import com.example.musiqal.downloadManager.data.DownloadableFiles


sealed class UniqueIdState {
    class Success(
        val downloadableFiles: Int,
    ) : UniqueIdState()

    object Idel : UniqueIdState()
    object Loading : UniqueIdState()
    class Failed(val errorMessgae: String) : UniqueIdState()
}
