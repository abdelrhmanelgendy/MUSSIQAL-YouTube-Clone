package com.example.musiqal.downloadManager.mvi.viewModels.intents

import com.example.musiqal.downloadManager.data.DownloadableFiles


sealed class MultipleDownLoadFileState {
    class Success(
        val downloadableFiles: List<DownloadableFiles>,
    ) : MultipleDownLoadFileState()

    object Idel : MultipleDownLoadFileState()
    object Loading : MultipleDownLoadFileState()
    class Failed(val errorMessgae: String) : MultipleDownLoadFileState()
}
