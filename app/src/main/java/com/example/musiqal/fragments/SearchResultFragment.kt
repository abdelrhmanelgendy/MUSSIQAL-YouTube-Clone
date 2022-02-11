package com.example.musiqal.fragments

import SearchResultAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musiqal.R
import com.example.musiqal.databinding.FragmentSerarchResultBinding
import com.example.musiqal.datamodels.youtubeApiSearchForVideo.Item
import com.example.musiqal.datamodels.youtubeApiSearchForVideo.ItemConverters
import com.example.musiqal.datamodels.youtubeItemInList.*
import com.example.musiqal.search.SearchActivity
import com.example.musiqal.search.mvi.SearchViewModel
import com.example.musiqal.search.mvi.YoutubeSearchViewState
import com.example.musiqal.util.Constants
import com.example.musiqal.util.OnAudioInPlaylistClickListner
import com.example.musiqal.util.OnSearchedItemClickListner
import com.example.musiqal.viewModels.MainViewModel
import com.example.musiqal.viewModels.viewStates.YoutubeVideoDurationViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed


@AndroidEntryPoint
class SearchResultFragment : Fragment(), OnSearchedItemClickListner {

    companion object {
        val LAST_SEARCH_QYERY_KEY = "last_search_query"
    }

    private val TAG = "SerarchResultFragment11"
    val binding: FragmentSerarchResultBinding by lazy {
        FragmentSerarchResultBinding.inflate(layoutInflater)
    }
    val searchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }
    val mianViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    lateinit var searchResultAdapter: SearchResultAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var onAudioInPlaylistClickListner: OnAudioInPlaylistClickListner
    val SEARCH_NAME = "title"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnAudioInPlaylistClickListner) {
            onAudioInPlaylistClickListner = context
        } else {
            throw Throwable("must implement onItemVideoInPlayListClickListner")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideProgressBar(false)
        linearLayoutManager = LinearLayoutManager(requireContext())
        searchResultAdapter = SearchResultAdapter(requireContext(), this)
        binding.searchResultFragmentRecyclerView.adapter = searchResultAdapter
        binding.searchResultFragmentRecyclerView.layoutManager = linearLayoutManager

        val searchTitle = arguments?.getString(SEARCH_NAME)!!
        searForQuery(searchTitle)
        setUpTextViewTitle(searchTitle)


    }

    private fun dataGettedSuccessfully(items: List<Item>) {

        mapNoralrItemToItemWithDuration(items.toMutableList())


    }

    private fun setUpRecyclerView(items: List<Item>) {
        searchResultAdapter.setList(items)

        Log.d(TAG, "setUpRecyclerView: " + items.get(0).videoDuration)

    }

    private fun hideProgressBar(isProgressBarVisible: Boolean) {
        binding.searchResultFragmentProgressBarWaiting.isVisible = !isProgressBarVisible
        binding.searchResultFragmentRecyclerView.isVisible = isProgressBarVisible

    }

    private fun setUpTextViewTitle(searchTitle: String) {
        binding.searchResultFragmentTVSearch.text = searchTitle
        binding.searchResultFragmentTVSearch.setOnClickListener {
//            startSearchingActivityWithLastSearching(searchTitle)
        }
        binding.searchResultFragmentImgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun startSearchingActivityWithLastSearching(searchTitle: String) {
        val searchingIntent = Intent(requireActivity(), SearchActivity::class.java)
        searchingIntent.putExtra(LAST_SEARCH_QYERY_KEY, searchTitle)
        requireActivity().startActivityForResult(searchingIntent, 51)

    }

    private fun convertToListOfItem(listOfItemsTexted: String): List<Item> {
        return ItemConverters().convertToItems(listOfItemsTexted)
    }


    fun bundleInstance(searchName: String) {
        arguments = Bundle().apply {
            putString(SEARCH_NAME, searchName)
        }

    }


    override fun onSearchResultClick(
        item: Item,
        _listOfYoutubeItemsInPlaylists: MutableList<Item>,
        position: Int
    ) {
        val convertNormalLISToFItemToYoutubeListOfItemInPlaylist =
            convertNormalLISToFItemToYoutubeListOfItemInPlaylist(_listOfYoutubeItemsInPlaylists)
        val convertedItem: com.example.musiqal.datamodels.youtubeItemInList.Item =
            convertNormalItemToYoutubeItemInPlaylist(item)
        onAudioInPlaylistClickListner.onItemClick(
            convertedItem,
            convertNormalLISToFItemToYoutubeListOfItemInPlaylist.toMutableList(),
            position,
            convertNormalLISToFItemToYoutubeListOfItemInPlaylist.get(position).snippet.channelTitle
        )


        Log.d(TAG, "onSearchResultClick: " + convertedItem.videoDuration)


    }

    private fun mapNoralrItemToItemWithDuration(_listOfYoutubeItemsInPlaylists: MutableList<Item>) {
//

        getAllVideosDurationsInPLayList(_listOfYoutubeItemsInPlaylists)
//        searchResultAdapter.setDurationsList(items.)
    }

    private fun getAllVideosDurationsInPLayList(
        items: MutableList<Item>
    ) {
        mianViewModel.getVideoDuration(
            part = Constants.YOUTUBE_CONTENTDETAIL_PARTS,
            items.map { i -> i.id.videoId },
            Constants.getRandomYoutubeDataKey(requireContext())

        )
        lifecycleScope.launchWhenStarted {
            mianViewModel.youTubeVideoDurationStateFlow.collectIndexed { index, value ->
                when (value) {
                    is YoutubeVideoDurationViewState.Loading -> {
                        Log.d(TAG, "getAllVideosDurationsInPLayList: loading")
                    }
                    is YoutubeVideoDurationViewState.Success -> {
                        Log.d(TAG, "getAllVideosDurationsInPLayList: success${value.duration}")
                        replacingDurationOldWithNewDuration(value.duration, items)

                    }
                    is YoutubeVideoDurationViewState.Failed -> {
                        Log.d(TAG, "getAllVideosDurationsInPLayList: ${value.errorMessgae}")
                    }
                }

            }
        }
    }

    private fun replacingDurationOldWithNewDuration(
        duration: List<String>,
        items: MutableList<Item>,

        ) {
        items.forEachIndexed { index, currentItem ->
            currentItem.videoDuration = duration.get(index)
        }
        hideProgressBar(true)
        setUpRecyclerView(items)

    }

    private fun convertNormalLISToFItemToYoutubeListOfItemInPlaylist(_listOfYoutubeItemsInPlaylists: MutableList<Item>):
            List<com.example.musiqal.datamodels.youtubeItemInList.Item> {

        val map = _listOfYoutubeItemsInPlaylists.map { item ->
            val highThumbnailsUrl = item.snippet.thumbnails.high.url
            val defaultThumbnailsUrl = item.snippet.thumbnails.default.url
            val mediumhighThumbnailsUrl = item.snippet.thumbnails.medium.url
            val channelTitle = item.snippet.channelTitle
            val channelId = item.snippet.channelId
            val description = item.snippet.description
            val publishTime = item.snippet.publishTime
            val title = item.snippet.title
            val videoId = item.id.videoId
            Item(
                "-1",
                videoId,
                "-1",
                Snippet(
                    channelId,
                    channelTitle,
                    description,
                    "",
                    -1,
                    publishTime,
                    ResourceId("-1", videoId),
                    Thumbnails(
                        Default(-1, defaultThumbnailsUrl, -1),
                        High(-1, highThumbnailsUrl, -1),
                        Maxres(-1, highThumbnailsUrl, -1),
                        Medium(-1, mediumhighThumbnailsUrl, -1),
                        Standard(-1, highThumbnailsUrl, -1)
                    ),
                    title,
                    "-1",
                    "-1"
                ), "", item.videoDuration
            )
        }
        return map

    }

    private fun convertNormalItemToYoutubeItemInPlaylist(item: Item): com.example.musiqal.datamodels.youtubeItemInList.Item {

        val highThumbnailsUrl = item.snippet.thumbnails.high.url
        val defaultThumbnailsUrl = item.snippet.thumbnails.default.url
        val mediumhighThumbnailsUrl = item.snippet.thumbnails.medium.url
        val channelTitle = item.snippet.channelTitle
        val channelId = item.snippet.channelId
        val description = item.snippet.description
        val publishTime = item.snippet.publishTime
        val title = item.snippet.title
        val videoId = item.id.videoId


        return Item(
            "-1",
            videoId,
            "-1",
            Snippet(
                channelId,
                channelTitle,
                description,
                "",
                -1,
                publishTime,
                ResourceId("-1", videoId),
                Thumbnails(
                    Default(-1, defaultThumbnailsUrl, -1),
                    High(-1, highThumbnailsUrl, -1),
                    Maxres(-1, highThumbnailsUrl, -1),
                    Medium(-1, mediumhighThumbnailsUrl, -1),
                    Standard(-1, highThumbnailsUrl, -1)
                ),
                title,
                "-1",
                "-1"
            ), "", item.videoDuration
        )

    }

    fun getRandomApiKey(): String {
        return Constants.getRandomYoutubeDataKey(requireContext())

    }

    private fun searForQuery(searchTitle: String) {
        searchViewModel.serchForVideos(
            "snippet",
            "video",
            searchTitle,
            "50",
            Constants.musicYoutubeId.toString(),
            getRandomApiKey()
        )
        lifecycleScope.launchWhenStarted {
            searchViewModel.youtubeSearchStateFlow.collect { state ->
                when (state) {
                    is YoutubeSearchViewState.Loading -> {
                        hideProgressBar(false)
                    }
                    is YoutubeSearchViewState.Success -> {
                        Log.d(
                            TAG,
                            "searchYoutubeQuery: $searchTitle " + state.youtubeApiRequest.items.toString()
                        )
                        dataGettedSuccessfully(state.youtubeApiRequest.items)
                    }
                    is YoutubeSearchViewState.Failed -> {
                        Log.d(TAG, "searchYoutubeQuery: " + state.errorMessgae.toString())
                        searForQuery(searchTitle)
                        hideProgressBar(false)

                    }

                }
            }
        }
    }


}