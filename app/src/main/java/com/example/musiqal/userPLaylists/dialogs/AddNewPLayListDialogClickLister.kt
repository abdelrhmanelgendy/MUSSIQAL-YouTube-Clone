package com.example.musiqal.userPLaylists.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.example.musiqal.databinding.AddNewPlaylistDialogItemBinding

interface AddNewPLayListDialogClickLister {
    fun onOkButtonClick(txt: String)
    fun onCancelButtonClick(txt: String)
    fun itemAdded()
}