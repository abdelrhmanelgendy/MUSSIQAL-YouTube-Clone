package com.example.musiqal.fragments

import UserPlayListPreviewAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.musiqal.datamodels.youtubeItemInList.Item
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musiqal.customeMusicPlayer.util.ShuffleMode
import com.example.musiqal.databinding.FragmentUserPlaylistPreviewBinding
import com.example.musiqal.helpers.CalenderHelper
import com.example.musiqal.userPLaylists.model.UserPlayList
import com.example.musiqal.userPLaylists.mvi.UserPlayListViewModel
import com.example.musiqal.userPLaylists.mvi.viewStates.UserPlayListItemState
import com.example.musiqal.util.GlideLoader
import com.example.musiqal.util.OnAudioInPlaylistClickListner
import com.example.musiqal.util.OnItemVideoInPlayListClickListner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.random.Random


class UserPlayListPreviewFragment : Fragment(), OnItemVideoInPlayListClickListner {
    lateinit var binding: FragmentUserPlaylistPreviewBinding
    val userPlayListViewModel: UserPlayListViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserPlayListViewModel::class.java)
    }
    lateinit var onAudioInPlaylistClickListner: OnAudioInPlaylistClickListner

    lateinit var userPLayList: UserPlayList

    companion object {
        val PARAM_KEY = "user_playlist_key"

        @JvmStatic
        fun newInstance(param1: String) =
            UserPlayListPreviewFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_KEY, param1)

                }
            }
    }

    lateinit var userPlayListPreviewAdapter: UserPlayListPreviewAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserPlaylistPreviewBinding.inflate(layoutInflater)
        progressBarVisibility(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (child in binding.UserPLaylistPreviewFragmentLinearLayout.children) {
            child.setOnClickListener {}
        }
        userPlayListPreviewAdapter = UserPlayListPreviewAdapter(requireContext(), this)
        linearLayoutManager = LinearLayoutManager(requireContext())
        arguments?.let {
            val userPLaylistName = it.getString(PARAM_KEY)!!
            getPlayListByItsName(userPLaylistName)

        }
        setButtonsListeners()
    }

    private fun setButtonsListeners() {
        binding.UserPLaylistPreviewFragmentBtnPlayAllList.setOnClickListener {
            if (::userPLayList.isInitialized && userPLayList.playListItems.size > 0) {
                val position = 0
                val item = userPLayList.playListItems.get(position)

                onAudioInPlaylistClickListner.onItemClick(item,userPLayList.playListItems.toMutableList(),position,userPLayList.playListName,ShuffleMode.Shuffle)
            }
        }
        binding.UserPLaylistPreviewFragmentBtnPlayShuffle.setOnClickListener {
            if (::userPLayList.isInitialized && userPLayList.playListItems.size > 0) {
                val position = Random.nextInt(userPLayList.playListItems.size - 1)
                val item = userPLayList.playListItems.get(position)

                onAudioInPlaylistClickListner.onItemClick(item,userPLayList.playListItems.toMutableList(),position,userPLayList.playListName,ShuffleMode.NoShuffle)
            }
        }
        binding.UserPLaylistPreviewFragmentBtnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getPlayListByItsName(userPLaylistName: String) {
        userPlayListViewModel.getPlayListByName(userPLaylistName)
        lifecycleScope.launchWhenStarted {
            userPlayListViewModel.userPLayListItem.collectIndexed { index, value ->
                when (value) {
                    is UserPlayListItemState.Loading -> {
                    }
                    is UserPlayListItemState.Success -> {
                        if (index == 1) {
                            setupRecyclerView(value.listOfPlayLists.playListItems)
                            setUpViewsData(value.listOfPlayLists)
                            userPLayList = value.listOfPlayLists
                        }
                    }
                    is UserPlayListItemState.Failed -> {
                        Log.d("TAG", "getPlayListByItsName: " + value.errorMessgae)
                    }
                }
            }
        }

    }

    private fun setUpViewsData(userPlayList: UserPlayList) {
        val calenderHelper = CalenderHelper()
        calenderHelper.setCalender(userPlayList.playLisLastUpdate.toLong())

        binding.UserPLaylistPreviewFragmentTVPlayListName.text = userPlayList.playListName
        binding.UserPLaylistPreviewFragmentTVPlayListPublishDate.text =
            "Last updated  " + calenderHelper.toString()
        GlideLoader()
            .loadImage(
                binding.UserPLaylistPreviewFragmentImagViewPlayListImage,
                userPlayList.playListCoverUrl
            )
        lifecycleScope.launch(Dispatchers.Main)
        {
            delay(200)
            progressBarVisibility(false)
        }

    }

    fun setupRecyclerView(items: List<Item>) {
        Log.d("TAG", "setupRecyclerView: " + items)
        userPlayListPreviewAdapter.setListOfItems(items)
        binding.UserPLaylistPreviewFragmentRecyclerView
            .also {
                it.adapter = userPlayListPreviewAdapter
                it.layoutManager = linearLayoutManager
            }

    }

    override fun onVideoClick(
        item: Item,
        idsList: List<String>,
        _listOfYoutubeItemsInPlaylists: MutableList<Item>,
        position: Int,

    ) {
        Log.d("TAG", "onVideoClick: " + item)
        onAudioInPlaylistClickListner.onItemClick(
            item,
            _listOfYoutubeItemsInPlaylists,
            position,
            item.snippet.channelTitle
        )
    }

    fun progressBarVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.UserPLaylistPreviewFragmentProgreeBar.visibility = View.VISIBLE
            binding.UserPLaylistPreviewFragmentLinearLayout.visibility = View.GONE
        } else {
            binding.UserPLaylistPreviewFragmentProgreeBar.visibility = View.GONE
            binding.UserPLaylistPreviewFragmentLinearLayout.visibility = View.VISIBLE
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnAudioInPlaylistClickListner) {
            onAudioInPlaylistClickListner = context
        } else {
            throw Exception("you must implement On Item Listener")
        }

    }
}
