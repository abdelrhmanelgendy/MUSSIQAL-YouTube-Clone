package com.example.musiqal.downloadManager.mvi.viewModels.intents

import com.example.musiqal.downloadManager.data.DownloadableFiles


sealed class SingleDownLoadFileState {
    class Success(
        val downloadableFiles: DownloadableFiles,
    ) : SingleDownLoadFileState()

    object Idel : SingleDownLoadFileState()
    object Loading : SingleDownLoadFileState()
    class Failed(val errorMessgae: String) : SingleDownLoadFileState()
}
