package com.example.musiqal.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import dagger.hilt.android.AndroidEntryPoint

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.musiqal.databinding.ActivityTestBinding
import com.example.musiqal.models.youtubeItemInList.Item
import com.example.musiqal.userPLaylists.dialogs.AddNewPLayListDialog
import com.example.musiqal.userPLaylists.dialogs.AddNewPLayListDialogClickLister
import com.example.musiqal.userPLaylists.dialogs.UserPLaylistsDialog
import com.example.musiqal.userPLaylists.model.PlaylistItem
import com.example.musiqal.userPLaylists.model.UserPlayList
import com.example.musiqal.userPLaylists.mvi.UserPlayListViewModel
import com.example.musiqal.userPLaylists.mvi.viewStates.AllUserPlayListsNamesState
import com.example.musiqal.userPLaylists.mvi.viewStates.ListOfUserPlayListsState
import com.example.musiqal.util.MakingToast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Test : AppCompatActivity() {
    private val TAG = "Test"
    lateinit var binding: ActivityTestBinding
    val userPlayListViewModel: UserPlayListViewModel by lazy {
        ViewModelProvider(this).get(UserPlayListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnAddNewList.setOnClickListener {
            userPlayListViewModel.insertNewPLayList(
                UserPlayList(
                    "si", "sd", "ws", listOf(
                        Item(),
                        Item()
                    )
                )
            )

        }
        binding.showLists.setOnClickListener {

//            val videoItem = Item()
//            val userPLaylistsDialog = UserPLaylistsDialog(this)
//            userPLaylistsDialog.createDialog(videoItem)
            userPlayListViewModel.getAllPlayLists()
            lifecycleScope.launch {
                userPlayListViewModel.listOfUserPLayLists.collect {
                    when(it)
                    {
                        is ListOfUserPlayListsState.Success->
                        {
                            Log.d(TAG, "onCreate: "+it.listOfPlayLists.size)
                            Log.d(TAG, "onCreate: "+it.listOfPlayLists.toString())
                        }
                    }
                }
            }
        }

    }


}