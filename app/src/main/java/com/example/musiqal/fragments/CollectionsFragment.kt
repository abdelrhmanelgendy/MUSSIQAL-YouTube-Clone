package com.example.musiqal.fragments

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
import com.example.musiqal.models.youtubeItemInList.Item
import com.example.musiqal.recyclerViewAdapters.collectionsAdapter.util.OnTrackClickListener
import com.example.musiqal.util.MakingToast
import com.example.musiqal.util.OnAudioInPlaylistClickListner
import com.example.musiqal.viewModels.MainViewModel
import com.example.musiqal.viewModels.viewStates.SavedPlayListViewState
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import java.lang.Exception

class CollectionsFragment : Fragment(), OnTrackClickListener {

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
    private val TAG = "CollectionsFragmentTAG"
    lateinit var onAudioInPlaylistClickListner: OnAudioInPlaylistClickListner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCollectionsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewListeners()
        getAllPLayedTracksFromDataBase()


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
        historyOfPlayedTracksAdapter.setList(list)
        progressBarVisibility(false)

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



    fun progressBarVisibility(isVisible:Boolean)
    {
        if (isVisible)
        {
            binding.collectionFragmentProgressBar.visibility=View.VISIBLE
            binding.collectionFragmentLinearLayoutAllViews.visibility=View.GONE
        }
        else
        {
            binding.collectionFragmentProgressBar.visibility=View.GONE
            binding.collectionFragmentLinearLayoutAllViews.visibility=View.VISIBLE
        }
    }
}