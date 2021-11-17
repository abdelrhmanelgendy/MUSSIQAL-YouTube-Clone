package com.example.musiqal.userPLaylists.dialogs

import UserPLayListDialogsAdapter
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musiqal.databinding.PlayListsDialogBinding
import com.example.musiqal.datamodels.youtubeItemInList.Item
import com.example.musiqal.userPLaylists.dialogs.adapter.OnPlayListClickListener
import com.example.musiqal.userPLaylists.model.UserPlayList
import com.example.musiqal.userPLaylists.mvi.UserPlayListViewModel
import com.example.musiqal.userPLaylists.mvi.viewStates.AllUserPlayListsNamesState
import com.example.musiqal.userPLaylists.mvi.viewStates.ListOfUserPlayListsState
import com.example.musiqal.userPLaylists.mvi.viewStates.UserPlayListItemState
import com.example.musiqal.util.ImageUrlUtil
import com.example.musiqal.util.MakingToast
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectIndexed

class UserPLaylistsDialog(private val context: Context) : OnPlayListClickListener,
    AddNewPLayListDialogClickLister {
    private val TAG = "UserPLaylistsDialog"
    private var playListsDialog: Dialog
    private var binding: PlayListsDialogBinding

    private lateinit var videoItem: Item

    val userPlaysListViewModel: UserPlayListViewModel by lazy {
        ViewModelProvider((context as AppCompatActivity)).get(UserPlayListViewModel::class.java)
    }

    init {
        val layoutInflater = LayoutInflater.from(context)
        binding = PlayListsDialogBinding.inflate(layoutInflater)
        binding.addVideoToPLayListDialogBtnCreateNewPlayList.setOnClickListener {
            openAddingNewPlayListDialog()
        }
        playListsDialog = Dialog(context)
        initializeDialog()
        getAllUserPLayLists()
    }


    fun createDialog(item: Item) {
        playListsDialog.show()
        this.videoItem = item
    }

    private fun getAllUserPLayLists() {

        CoroutineScope(Dispatchers.Main)
            .launch {
                userPlaysListViewModel.getAllPlayLists()
                userPlaysListViewModel.listOfUserPLayListsStateFLow.collectIndexed { index, event ->
                    when (event) {
                        is ListOfUserPlayListsState.Loading -> {
                            Log.d(TAG, "getAllUserPLayLists: loading")
                        }
                        is ListOfUserPlayListsState.Success -> {
                            if (index == 1) {
                                Log.d(
                                    TAG,
                                    "getAllUserPLayLists: " + event.listOfPlayLists.toString()
                                )
                                setUpRecyclerViewPlayList(event.listOfPlayLists)
                            }
                        }
                        is ListOfUserPlayListsState.Failed -> {
                            Log.d(TAG, "getAllUserPLayLists: " + event.errorMessgae)
                        }

                    }

                }

            }

    }

    private fun setUpRecyclerViewPlayList(listofPlaylists: List<UserPlayList>) {
        val layoutManager = LinearLayoutManager(context)

        val userPLaylistDialog = UserPLayListDialogsAdapter(context, this)
        userPLaylistDialog.setList(listofPlaylists)

        binding.addVideoToPLayListDialogRecyclerViewUserPLayLisst.also {
            it.layoutManager = layoutManager
            it.adapter = userPLaylistDialog
        }

    }

    private fun initializeDialog() {
        playListsDialog.setContentView(binding.root)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        playListsDialog.window?.attributes = layoutParams
        binding.addVideoToPLayListDialogImgClose.setOnClickListener {
            playListsDialog.dismiss()
        }

    }

    override fun onPLaylistClick(userPlayList: UserPlayList) {
        addVideoToThePlayList(userPlayList.playListName)
    }

    private fun addVideoToThePlayList(playListName: String) {
        getPLayListByName(playListName)
    }

    private fun getPLayListByName(playListName: String) {
        userPlaysListViewModel.getPlayListByName(playListName)
        CoroutineScope(Dispatchers.IO)
            .launch {
                userPlaysListViewModel.userPLayListItem.collectIndexed { index, event ->

                    when (event) {
                        is UserPlayListItemState.Loading -> {
                        }
                        is UserPlayListItemState.Success -> {
                            if (index == 1) {
                                getItemsAsListOfItems(event.listOfPlayLists, playListName)
                            }
                        }
                        is UserPlayListItemState.Failed -> {
                            Log.d(TAG, "getPLayListByName: " + event.errorMessgae)
                        }
                    }

                }
            }

    }

    private fun getItemsAsListOfItems(
        listOfPlayLists: UserPlayList,
        playListName: String
    ) {
        if (checkIfTrackAlreadyAddedToThisPlaylist(listOfPlayLists, playListName, this.videoItem)) {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(
                    context,
                    "this track already added to this $playListName playlist",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } else {
            val videoItem = this.videoItem
            val itemsMutableList = listOfPlayLists.playListItems.toMutableList()
            itemsMutableList.add(0, videoItem)

            val playListCoverUrl = ImageUrlUtil.getMaxResolutionImageUrl(this.videoItem)
            val playListUpdateTime = System.currentTimeMillis().toString()
            userPlaysListViewModel.deletePlayListByName(playListName)
            userPlaysListViewModel.insertNewPLayList(
                UserPlayList(
                    playListName,
                    playListUpdateTime,
                    playListCoverUrl,
                    itemsMutableList
                )
            )
            CoroutineScope(Dispatchers.Main).launch {
                MakingToast(context)
                    .toast("Video added", MakingToast.LENGTH_SHORT)
            }
        }


    }

    private fun checkIfTrackAlreadyAddedToThisPlaylist(
        listOfPlayLists: UserPlayList,
        playListName: String,
        videoItem: Item
    ): Boolean {
        val videoId = videoItem.snippet.resourceId.videoId
        if (videoId in listOfPlayLists.playListItems.map { i -> i.snippet.resourceId.videoId }) {
            return true
        }
        return false


    }

    lateinit var addPLayListDialod: AddNewPLayListDialog
    private fun openAddingNewPlayListDialog() {
        playListsDialog.dismiss()
        addPLayListDialod = AddNewPLayListDialog(context)
        addPLayListDialod.createDialog(this)
        addPLayListDialod.showDialog(false)
    }

    override fun onOkButtonClick(txt: String) {
        createPlayList(txt)

    }

    override fun onCancelButtonClick(txt: String) {
        playListsDialog.show()
    }

    override fun itemAdded() {

    }


    private fun createPlayList(txt: String) {
        getAllPlayLists(txt)
    }

    private fun getAllPlayLists(txt: String) {
        checkPLayListConflictWithOtherPlatLists(txt)
    }


    private fun checkPLayListConflictWithOtherPlatLists(txt: String) {

        Log.d(TAG, "checkPLayListConflictWithOtherPlatLists: " + txt)
        userPlaysListViewModel.getAllPlayListsNames()
        CoroutineScope(Dispatchers.Main).launch {
            userPlaysListViewModel.userPLayListsNames.collectIndexed { index, value ->
                when (value) {
                    is AllUserPlayListsNamesState.Loading -> {
                        Log.d(TAG, "getAllPlayLists: loading")
                    }
                    is AllUserPlayListsNamesState.Success -> {
                        if (index == 1) {
                            checkConflict(value.listOfNames, txt)
                            Log.d(
                                TAG,
                                "getAllPlayLists:  success  $index" + value.listOfNames.toString()
                            )

                        }

                    }
                    is AllUserPlayListsNamesState.Failed -> {
                        Log.d(TAG, "getAllPlayLists: failed" + value.errorMessgae)
                    }
                }
            }
        }
    }

    private fun checkConflict(listOfNames: List<String>, txt: String) {
        Log.d(TAG, "checkConflict: " + txt)
        Log.d(TAG, "checkConflict: " + listOfNames.toString())
        if (txt in listOfNames) {

            MakingToast(context).toast(
                "PlayList already exists, choose another name",
                MakingToast.LENGTH_SHORT
            )

            return
        } else {
            addNewPLayList(txt)
        }


    }

    private fun addNewPLayList(playlistName: String) {
        val currentTime = System.currentTimeMillis().toString()
        val newUserList = UserPlayList(
            playlistName, currentTime,
            "",
            listOf()
        )
        userPlaysListViewModel.insertNewPLayList(newUserList)
        MakingToast(context).toast("Playlist added", MakingToast.LENGTH_SHORT)
        addPLayListDialod.dismissDialog()

        playListsDialog.show()
        getAllUserPLayLists()


    }


}