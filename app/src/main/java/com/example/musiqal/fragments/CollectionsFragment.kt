package com.example.musiqal.fragments

import AllPlaylistsAdapter
import HistoryOfPlayedTracksAdapter
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musiqal.databinding.FragmentCollectionsBinding
import com.example.musiqal.fragments.util.OnCollectionFragmentListeners
import com.example.musiqal.datamodels.youtubeItemInList.Item
import com.example.musiqal.recyclerViewAdapters.collectionsAdapter.util.OnTrackClickListener
import com.example.musiqal.ui.MainActivity
import com.example.musiqal.userPLaylists.dialogs.adapter.OnPlayListClickListener
import com.example.musiqal.userPLaylists.model.UserPlayList
import com.example.musiqal.userPLaylists.mvi.UserPlayListViewModel
import com.example.musiqal.userPLaylists.mvi.viewStates.ListOfUserPlayListsState
import com.example.musiqal.util.OnAudioInPlaylistClickListner
import com.example.musiqal.viewModels.MainViewModel
import com.example.musiqal.viewModels.viewStates.SavedPlayListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import java.lang.Exception

class CollectionsFragment : Fragment(), OnTrackClickListener, OnPlayListClickListener {

    lateinit var layoutManager: LinearLayoutManager
    lateinit var onCollectionFragmentListeners: OnCollectionFragmentListeners
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is OnCollectionFragmentListeners) {
            onCollectionFragmentListeners = activity
        } else {
            throw Exception("")
        }
    }

    private lateinit var itemsList: List<Item>
    lateinit var binding: FragmentCollectionsBinding
    val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    val userPlayListViewModel: UserPlayListViewModel by lazy {
        ViewModelProvider(requireActivity()).get(UserPlayListViewModel::class.java)
    }
    private val TAG = "CollectionsFragmentTAG"
    lateinit var onAudioInPlaylistClickListner: OnAudioInPlaylistClickListner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCollectionsBinding.inflate(layoutInflater)
        swipeRefresherVisibility(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewListeners()
        layoutManager = LinearLayoutManager(requireContext())
        getAllPLayedTracksFromDataBase()
        getAllUserPLaylistsFromDB()
        binding.collectionFragmentSwipeRefresher.setOnRefreshListener {
            swipeRefresherVisibility(true)
            getAllPLayedTracksFromDataBase()
            getAllUserPLaylistsFromDB()

        }


    }

    private fun getAllUserPLaylistsFromDB() {
        userPlayListViewModel.getAllPlayLists()
        lifecycleScope.launchWhenStarted {
            userPlayListViewModel.listOfUserPLayListsStateFLow
                .collectIndexed { index, value ->
                    when (value) {
                        is ListOfUserPlayListsState.Success -> {
                            if (index == 1) {
                                Log.d(
                                    TAG,
                                    "getAllUserPLaylistsFromDB: " + value.listOfPlayLists.size
                                )
                                val userLists = value.listOfPlayLists
                                setupUserPlaylistsRecyclerView(userLists)
                            }
                        }
                        is ListOfUserPlayListsState.Loading -> {
                            Log.d(TAG, "getAllUserPLaylistsFromDB: loading")
                        }
                        is ListOfUserPlayListsState.Failed -> {
                            Log.d(TAG, "getAllUserPLaylistsFromDB: " + value.errorMessgae)
                        }

                    }
                }
        }
    }


    fun setupUserPlaylistsRecyclerView(list: List<UserPlayList>) {
        val allPlaylistsAdapter = AllPlaylistsAdapter(requireContext(), this)
        allPlaylistsAdapter.setList(list)
        binding.collectionFragmentRecyclerViewAllPLaylists.also {
            it.layoutManager = layoutManager
            it.adapter = allPlaylistsAdapter
        }
        lifecycleScope.launch(Dispatchers.Main) {
            delay(200)
            swipeRefresherVisibility(false)
        }


    }

    private fun setViewListeners() {
        binding.collectionFragmentLayoutBtnOpenHistory.setOnClickListener {
            openRecentlyPlayedFragment()
        }

        binding.collectionFragmentLayoutRecentlyPLayed.setOnClickListener {
            openRecentlyPlayedFragment()
        }
    }

    private fun openRecentlyPlayedFragment() {
        if (itemsList.size != 0) {
            onCollectionFragmentListeners.onPlayedTracksClick(this.itemsList)

        }
    }

    private fun getAllPLayedTracksFromDataBase() {
        mainViewModel.getAllPLayedTrackHistory()
        lifecycleScope.launch {
            mainViewModel.savedPlayedTraksStateFlow.collectIndexed { index, event ->
                when (event) {
                    is SavedPlayListViewState.Loading -> {
                        Log.d(TAG, "getAllPLayedTracksFromDataBase: loading")
                    }
                    is SavedPlayListViewState.Success -> {
                        if (index == 1) {
                            Log.d(
                                TAG,
                                "getAllPLayedTracksFromDataBase: succedd ${index}" + event.item.size
                            )

                            setUpDataIntoRecyclerView(event.item)
                        }
                    }
                    is SavedPlayListViewState.Failed -> {
                        Log.d(TAG, "getAllPLayedTracksFromDataBase: " + event.errorMessgae)
                    }
                }
            }
        }
    }

    fun setUpDataIntoRecyclerView(list: List<Item>) {
        this.itemsList = list
        Log.d(TAG, "setUpDataIntoRecyclerView: setting")
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val historyOfPlayedTracksAdapter = HistoryOfPlayedTracksAdapter(requireContext(), this)
        binding.collectionFragmentRecyclerViewPlayedTracks.also {
            it.adapter = historyOfPlayedTracksAdapter
            it.layoutManager = layoutManager
        }
        historyOfPlayedTracksAdapter.setList(list.reversed())
    }

    override fun onVideoClick(
        item: Item,
        idsList: List<String>,
        _listOfYoutubeItemsInPlaylists: MutableList<Item>,
        position: Int
    ) {
        onAudioInPlaylistClickListner.onItemClick(
            item,
            _listOfYoutubeItemsInPlaylists,
            position,
            item.snippet.channelTitle
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnAudioInPlaylistClickListner) {
            onAudioInPlaylistClickListner = context
        } else {
            throw Exception("you must implement On Item Listener")
        }

    }


    fun swipeRefresherVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.collectionFragmentSwipeRefresher.isRefreshing = true
            binding.collectionFragmentLinearLayoutAllViews.visibility = View.GONE
        } else {
            binding.collectionFragmentSwipeRefresher.isRefreshing = false
            binding.collectionFragmentLinearLayoutAllViews.visibility = View.VISIBLE
        }
    }

    override fun onPLaylistClick(userPlayList: UserPlayList) {
        val frag=UserPlayListPreviewFragment.newInstance(
            userPlayList.playListName
        )
        (requireActivity() as MainActivity).setFragmentF(frag)
    }
}