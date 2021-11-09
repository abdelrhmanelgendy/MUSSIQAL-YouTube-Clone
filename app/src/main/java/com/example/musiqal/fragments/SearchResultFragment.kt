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
import com.example.musiqal.models.youtubeApiSearchForVideo.Item
import com.example.musiqal.models.youtubeApiSearchForVideo.ItemConverters
import com.example.musiqal.models.youtubeItemInList.*
import com.example.musiqal.search.SearchActivity
import com.example.musiqal.search.mvi.SearchViewModel
import com.example.musiqal.search.mvi.YoutubeSearchViewState
import com.example.musiqal.util.Constants
import com.example.musiqal.util.OnAudioInPlaylistClickListner
import com.example.musiqal.util.OnSearchedItemClickListner
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class SearchResultFragment : Fragment(), OnSearchedItemClickListner {

    companion object
    {
        val LAST_SEARCH_QYERY_KEY="last_search_query"
    }

    private val TAG = "SerarchResultFragment11"
    val binding: FragmentSerarchResultBinding by lazy {
        FragmentSerarchResultBinding.inflate(layoutInflater)
    }
    val searchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }
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
        hideProgressBar(true)
        setUpRecyclerView(items)

    }

    private fun setUpRecyclerView(items: List<Item>) {
        searchResultAdapter.setList(items)
        Log.d(TAG, "setUpRecyclerView: " + items)

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
        val searchingIntent=Intent(requireActivity(),SearchActivity::class.java)
        searchingIntent.putExtra(LAST_SEARCH_QYERY_KEY,searchTitle)
        requireActivity().startActivityForResult(searchingIntent,51)

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
        Log.d(TAG, "onSearchResultClick: " + item)
        val convertedItem: com.example.musiqal.models.youtubeItemInList.Item =
            convertNormalItemToYoutubeItemInPlaylist(item)
        val convertNormalLISToFItemToYoutubeListOfItemInPlaylist =
            convertNormalLISToFItemToYoutubeListOfItemInPlaylist(_listOfYoutubeItemsInPlaylists)
        onAudioInPlaylistClickListner.onItemClick(
            convertedItem,
            convertNormalLISToFItemToYoutubeListOfItemInPlaylist.toMutableList(),
            position,
            item.snippet.channelTitle
        )
    }

    private fun convertNormalLISToFItemToYoutubeListOfItemInPlaylist(_listOfYoutubeItemsInPlaylists: MutableList<Item>):
            List<com.example.musiqal.models.youtubeItemInList.Item> {

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
                ), ""
            )
        }
        return map

    }

    private fun convertNormalItemToYoutubeItemInPlaylist(item: Item): com.example.musiqal.models.youtubeItemInList.Item {

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
            ), ""
        )

    }

    fun getRandomApiKey(): String {
        val stringArray = resources.getStringArray(R.array.api_keys)
//        return stringArray.get(Random().nextInt(stringArray.size))
        return stringArray.get(0)
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