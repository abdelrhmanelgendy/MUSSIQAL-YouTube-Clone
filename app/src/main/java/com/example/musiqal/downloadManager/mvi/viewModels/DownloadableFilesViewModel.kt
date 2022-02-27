package com.example.musiqal.downloadManager.mvi.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiqal.downloadManager.data.DownLoadedState
import com.example.musiqal.downloadManager.data.DownloadableFiles
import com.example.musiqal.downloadManager.mvi.repository.DownloadableFilesMainRepository
import com.example.musiqal.downloadManager.mvi.viewModels.intents.MultipleDownLoadFileState
import com.example.musiqal.downloadManager.mvi.viewModels.intents.SingleDownLoadFileState
import com.example.musiqal.downloadManager.mvi.viewModels.intents.UniqueIdState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadableFilesViewModel @Inject
constructor(
    private val downloadableFilesMainRepository:
    DownloadableFilesMainRepository
) : ViewModel() {


    val specificFileByUniqueIdStateFlow: MutableStateFlow<SingleDownLoadFileState> =
        MutableStateFlow(SingleDownLoadFileState.Idel)
    val specificFileByDownloadIdStateFlow: MutableStateFlow<SingleDownLoadFileState> =
        MutableStateFlow(SingleDownLoadFileState.Idel)

    val filesByStateStateFlow: MutableStateFlow<MultipleDownLoadFileState> =
        MutableStateFlow(MultipleDownLoadFileState.Idel)
    val allFilesStateFlow: MutableStateFlow<MultipleDownLoadFileState> =
        MutableStateFlow(MultipleDownLoadFileState.Idel)


    val uniqueIdByDownloadManagerIdStateFlow: MutableStateFlow<UniqueIdState> =
        MutableStateFlow(UniqueIdState.Idel)


    fun insert(downloadableFiles: DownloadableFiles) {
        viewModelScope.launch {
            downloadableFilesMainRepository.insertNewDownload(downloadableFiles)
        }
    }


    fun changeTheStateOfFiles(uniqueId: Int, newState: DownLoadedState) {
        viewModelScope.launch {
            downloadableFilesMainRepository.changeTheStateOfFilesByUniquesId(uniqueId, newState.state)
        }
    }

    fun changeTheStateOfFilesByDownloadId(downloadI: Int, newState: DownLoadedState) {
        viewModelScope.launch {
            downloadableFilesMainRepository.changeTheStateOfFilesByDownloadId(downloadI, newState.state)
        }
    }

    fun getSpecificFileByUniqueId(uniqueId: Int) {
        viewModelScope.launch {
            specificFileByUniqueIdStateFlow.emit(SingleDownLoadFileState.Loading)
            try {
                val specificFileByUniqueId =
                    downloadableFilesMainRepository.getSpecificFileByUniqueId(uniqueId)
                specificFileByUniqueIdStateFlow.emit(
                    SingleDownLoadFileState.Success(
                        specificFileByUniqueId
                    )
                )
            } catch (e: Exception) {
                specificFileByUniqueIdStateFlow.emit(SingleDownLoadFileState.Failed(e.message!!))

            }

        }
    }

    fun getSpecificFileByDownloadId(downloadId: Int) {
        viewModelScope.launch {
            specificFileByDownloadIdStateFlow.emit(SingleDownLoadFileState.Loading)
            try {
                val specificFileByUniqueId =
                    downloadableFilesMainRepository.getSpecificFileByDownloadId(downloadId)
                specificFileByDownloadIdStateFlow.emit(
                    SingleDownLoadFileState.Success(
                        specificFileByUniqueId
                    )
                )
            } catch (e: Exception) {
                specificFileByDownloadIdStateFlow.emit(SingleDownLoadFileState.Failed(e.message!!))

            }

        }
    }


    fun getFilesByState(fileState: DownLoadedState) {
        viewModelScope.launch {
            filesByStateStateFlow.emit(MultipleDownLoadFileState.Loading)
            try {
                val specificFileByUniqueId =
                    downloadableFilesMainRepository.getFilesByState(fileState.state)
                filesByStateStateFlow.emit(
                    MultipleDownLoadFileState.Success(
                        specificFileByUniqueId
                    )
                )
            } catch (e: Exception) {
                filesByStateStateFlow.emit(MultipleDownLoadFileState.Failed(e.message!!))

            }

        }
    }

    fun getAllFiles() {
        viewModelScope.launch {
            allFilesStateFlow.emit(MultipleDownLoadFileState.Loading)
            try {
                val specificFileByUniqueId =
                    downloadableFilesMainRepository.getAllFiles()
                allFilesStateFlow.emit(
                    MultipleDownLoadFileState.Success(
                        specificFileByUniqueId
                    )
                )
            } catch (e: Exception) {
                allFilesStateFlow.emit(MultipleDownLoadFileState.Failed(e.message!!))

            }

        }
    }

    fun deleteFile(uniqueId: Int) {
        viewModelScope.launch {
            try {

                downloadableFilesMainRepository.deleteFilesFromDownload(uniqueId)

            } catch (e: Exception) {

            }

        }
    }


    fun getUniqueIdByDownloadManagerId(downloadId: Int) {
        viewModelScope.launch {

            uniqueIdByDownloadManagerIdStateFlow.emit(UniqueIdState.Loading)
        try {
            val uniqueIdByDownloadManagerId =
                downloadableFilesMainRepository.getUniqueIdByDownloadManagerId(downloadId)
            uniqueIdByDownloadManagerIdStateFlow.emit(UniqueIdState.Success(uniqueIdByDownloadManagerId))
        }catch (e:Exception)
        {
            uniqueIdByDownloadManagerIdStateFlow.emit(UniqueIdState.Failed(e.message.toString()))

        }
        }

    }
}

