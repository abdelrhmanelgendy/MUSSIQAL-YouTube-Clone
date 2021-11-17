package com.example.musiqal.fragments


import SpecialUserItemsAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musiqal.R
import com.example.musiqal.databinding.FragmentHomeBinding
import com.example.musiqal.datamodels.RandomSearchQueryWithImage
import com.example.musiqal.util.Constants
import com.example.musiqal.viewModels.viewStates.PlayListSearchViewState
import kotlinx.coroutines.flow.collect
import java.util.*

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musiqal.datamodels.youtubeApiSearchForPlayList.Item
import com.example.musiqal.datamodels.youtubeApiSearchForPlayList.Snippet
import com.example.musiqal.datamodels.youtubeApiSearchForPlayList.YoutubeApiSearchForPlayListRequest
import com.example.musiqal.search.SearchActivity
import com.example.musiqal.ui.MainActivity
import com.example.musiqal.util.OnPlayListItemClickListner
import com.example.musiqal.viewModels.PlayListsViewModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext


class HomeFragment() : Fragment(R.layout.fragment_home), OnPlayListItemClickListner {
    private val TAG = "HomeFragmentTAG"
    lateinit var homeBinding: FragmentHomeBinding
    val playListsViewModel: PlayListsViewModel by lazy {
        ViewModelProvider(requireActivity()).get(PlayListsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")
        homeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return homeBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
     lifecycleScope.launchWhenStarted {
         withContext(Dispatchers.Main)
         {
             setUpRamdomPlayList(Constants.PLAYLIST_MIXED_ID)
             setUpLatestPlayList(Constants.PLAYLIST_LATEST_ID)
             setUpTopPlayList(Constants.PLAYLIST_TOP_ID)
             setUpSingerOnePlayList(Constants.PLAYLIST_SINGERONE_ID)
             setUpSingerTwoPlayList(Constants.PLAYLIST_SINGERTWO_ID)
         }
     }


     initializeToolBar()
    }
    private fun initializeToolBar() {
        val searchClickLitnerObject=object :View.OnClickListener{
            override fun onClick(v: View?) {
                requireActivity().startActivityForResult(Intent(requireActivity(),SearchActivity::class.java),51)
            }
        }
        homeBinding.homeFragmentImgSearch.setOnClickListener(searchClickLitnerObject)
        homeBinding.homeFragmentImgSearch2.setOnClickListener(searchClickLitnerObject)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
    }


    private fun setUpRamdomPlayList(playListsId: Int) {
        val randomSearchForUser = getRandomSearchForUser()
        searchForPlayList(
            randomSearchForUser.searchQuery,
            playListsViewModel._randomPlayLitsStateFlow,
            playListsId
        )
        homeBinding.homeFragmentTVMovtivationTxt.text = randomSearchForUser.searchQuery

        lifecycleScope.launchWhenStarted {
            playListsViewModel._randomPlayLitsStateFlow.collect { event ->
                when (event) {
                    is PlayListSearchViewState.Loading -> {
                        Log.d(TAG, "setUpRamdomPlayList: loading")
                    }
                    is PlayListSearchViewState.Failed -> {
                        Log.d(TAG, "setUpRamdomPlayList: failed")
                        setUpRamdomPlayList(playListsId)
                    }
                    is PlayListSearchViewState.Success -> {

                        if (event.isFromInternet) {
                            saveThePlayListsToReUse(playListsId, event.youtubeApiRequest)
                        }
                        setUpDataIntoRecylerView(
                            event.youtubeApiRequest,
                            homeBinding.homeFragmentRecyclerViewSpecialRecycler,
                            R.layout.special_recycler_view_playlist_items
                        )
                    }


                }

            }
        }

    }


    private fun setUpTopPlayList(playListsId: Int) {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        searchForPlayList(
            "top songs",
            playListsViewModel._popularPlayLitsStateFlow,
            playListsId
        )
        homeBinding.playListMixedMusic.findViewById<TextView>(R.id.recyclerItemOfOtherRecycler_collectionName).text =
            "Top Songs"

        val circularImage =
            homeBinding.playListMixedMusic.findViewById<CircleImageView>(R.id.recyclerItemOfOtherRecycler_circularImage)
        lifecycleScope.launchWhenStarted {
            playListsViewModel._popularPlayLitsStateFlow.collect { event ->
                when (event) {
                    is PlayListSearchViewState.Loading -> {
                        Log.d(TAG, "setUpRamdomPlayList: loading")
                    }
                    is PlayListSearchViewState.Failed -> {
                        Log.d(TAG, "setUpRamdomPlayList: failed")
                        setUpTopPlayList(playListsId)
                    }
                    is PlayListSearchViewState.Success -> {

                        if (event.isFromInternet) {
                            saveThePlayListsToReUse(playListsId, event.youtubeApiRequest)
                        }
                        Glide.with(requireContext())
                            .load(getImageUrlQuality(event.youtubeApiRequest.items.get(0).snippet))
                            .into(circularImage)
                        setUpDataIntoRecylerView(
                            event.youtubeApiRequest,
                            homeBinding.playListMixedMusic.findViewById(R.id.recyclerItemOfOtherRecycler_recyclerView),
                            R.layout.special_recycler_view_playlist_items_no_border
                        )
                    }


                }

            }
        }

    }

    private fun setUpLatestPlayList(playListsId: Int) {
        searchForPlayList(
            "latest songs",
            playListsViewModel._latestPlayLitsStateFlow,
            playListsId
        )
        homeBinding.playListLatestMusic.findViewById<TextView>(R.id.recyclerItemOfOtherRecycler_collectionName).text =
            "Latest"

        val circularImage =
            homeBinding.playListLatestMusic.findViewById<CircleImageView>(R.id.recyclerItemOfOtherRecycler_circularImage)
        lifecycleScope.launchWhenStarted {
            playListsViewModel._latestPlayLitsStateFlow.collect { event ->
                when (event) {
                    is PlayListSearchViewState.Loading -> {
                        Log.d(TAG, "setUpRamdomPlayList: loading")
                    }
                    is PlayListSearchViewState.Failed -> {
                        Log.d(TAG, "setUpRamdomPlayList: failed")
                        setUpLatestPlayList(playListsId)
                    }
                    is PlayListSearchViewState.Success -> {

                        if (event.isFromInternet) {
                            saveThePlayListsToReUse(playListsId, event.youtubeApiRequest)
                        }
                        Glide.with(requireContext())
                            .load(getImageUrlQuality(event.youtubeApiRequest.items.get(0).snippet))
                            .into(circularImage)
                        setUpDataIntoRecylerView(
                            event.youtubeApiRequest,
                            homeBinding.playListLatestMusic.findViewById(R.id.recyclerItemOfOtherRecycler_recyclerView),
                            R.layout.special_recycler_view_playlist_items_no_border
                        )
                    }


                }

            }
        }

    }

    private fun getImageUrlQuality(snippet: Snippet): String {
        return snippet.thumbnails.high.url
    }

    private fun setUpSingerOnePlayList(playListsId: Int) {
        val singer = getFirstRandomSinger()
        searchForPlayList(
            singer,
            playListsViewModel._singer1PlayLitsStateFlow, playListsId
        )
        homeBinding.playListAnySingerOneMusic.findViewById<TextView>(R.id.recyclerItemOfOtherRecycler_collectionName).text =
            singer

        val circularImage =
            homeBinding.playListAnySingerOneMusic.findViewById<CircleImageView>(R.id.recyclerItemOfOtherRecycler_circularImage)
        lifecycleScope.launchWhenStarted {
            playListsViewModel._singer1PlayLitsStateFlow.collect { event ->
                when (event) {
                    is PlayListSearchViewState.Loading -> {
                        Log.d(TAG, "setUpRamdomPlayList: loading")
                    }
                    is PlayListSearchViewState.Failed -> {
                        Log.d(TAG, "setUpRamdomPlayList: failed")
                        setUpSingerOnePlayList(playListsId)
                    }
                    is PlayListSearchViewState.Success -> {

                        if (event.isFromInternet) {
                            saveThePlayListsToReUse(playListsId, event.youtubeApiRequest)
                        }
                        Glide.with(requireContext())
                            .load(getImageUrlQuality(event.youtubeApiRequest.items.get(0).snippet))
                            .into(circularImage)
                        setUpDataIntoRecylerView(
                            event.youtubeApiRequest,
                            homeBinding.playListAnySingerOneMusic.findViewById(R.id.recyclerItemOfOtherRecycler_recyclerView),
                            R.layout.special_recycler_view_playlist_items_no_border
                        )
                    }


                }

            }
        }


    }

    private fun setUpSingerTwoPlayList(playListsId: Int) {
        val singer = getSecondRandomSinger()
        searchForPlayList(
            singer,
            playListsViewModel._singer2PlayLitsStateFlow, playListsId
        )

        homeBinding.playListAnySingerTwoMusic.findViewById<TextView>(R.id.recyclerItemOfOtherRecycler_collectionName).text =
            singer

        val circularImage =
            homeBinding.playListAnySingerTwoMusic.findViewById<CircleImageView>(R.id.recyclerItemOfOtherRecycler_circularImage)
        lifecycleScope.launchWhenStarted {
            playListsViewModel._singer2PlayLitsStateFlow.collect { event ->
                when (event) {
                    is PlayListSearchViewState.Loading -> {
                        Log.d(TAG, "setUpRamdomPlayList: loading")
                    }
                    is PlayListSearchViewState.Failed -> {
                        Log.d(TAG, "setUpRamdomPlayList: failed")
                        setUpSingerTwoPlayList(playListsId)
                    }
                    is PlayListSearchViewState.Success -> {
                        if (event.isFromInternet) {
                            saveThePlayListsToReUse(playListsId, event.youtubeApiRequest)
                        }

                        Glide.with(requireContext())
                            .load(getImageUrlQuality(event.youtubeApiRequest.items.get(0).snippet))
                            .into(circularImage)
                        setUpDataIntoRecylerView(
                            event.youtubeApiRequest,
                            homeBinding.playListAnySingerTwoMusic.findViewById(R.id.recyclerItemOfOtherRecycler_recyclerView),
                            R.layout.special_recycler_view_playlist_items_no_border
                        )

                    }


                }

            }
        }


    }

    fun setUpDataIntoRecylerView(
        youtubeApiSearchForPlayListRequest: YoutubeApiSearchForPlayListRequest,
        recyclerView: RecyclerView,
        viewID: Int
    ) {
        val linearLayout =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = SpecialUserItemsAdapter(
            requireContext(),
            viewID,
            youtubeApiSearchForPlayListRequest.items,
            this
        )
        recyclerView.layoutManager = linearLayout
        recyclerView.adapter = adapter


    }

    private fun saveThePlayListsToReUse(
        id: Int,
        youtubeApiSearchForPlayListRequest: YoutubeApiSearchForPlayListRequest
    ) {
        //
        youtubeApiSearchForPlayListRequest.id = id
        playListsViewModel.deletePlayList(id)
        playListsViewModel.insertPlaylist(youtubeApiSearchForPlayListRequest)
    }


    fun searchForPlayList(
        searchQuery: String,
        mutableStateFlow: MutableStateFlow<PlayListSearchViewState>,
        playListsId: Int
    ) {
        //first chech if user first open app
        val specifyIfUpdateUserDataOfNotByRandomValues = Random().nextInt(50)
        if (specifyIfUpdateUserDataOfNotByRandomValues > 60) {

            Log.d(TAG, "searchForPlayList: ")
            playListsViewModel.serchForRandomPlaylists(
                "snippet",
                "playlist",
                searchQuery,
                "20",
                Constants.musicYoutubeId.toString(),
                getRandomApiKey(),
                mutableStateFlow
            )
        } else {

            Log.d(TAG, "searchForPlayList: ")
            playListsViewModel.getRandomsFromDataBase(
                playListsId, mutableStateFlow

            )
        }
    }


    fun getRandomSearchForUser(): RandomSearchQueryWithImage {
        val listSize = Constants.mapOfYoutubeSearchs.size
        val ramdomIndexOfSearchQuery = Random().nextInt(listSize)
        val selectedListOfImage = Constants.mapOfYoutubeSearchs.get(ramdomIndexOfSearchQuery)
        val ramdomIndexOfSearchImage = Random().nextInt(selectedListOfImage.imageList.size)
        val randomSelectedImage = selectedListOfImage.imageList.get(ramdomIndexOfSearchImage)
        val randomSelectedQuery =
            Constants.mapOfYoutubeSearchs.get(ramdomIndexOfSearchQuery).searchQuery
        val imgList = listOf(randomSelectedImage)
        return (RandomSearchQueryWithImage(randomSelectedQuery, imgList))
    }

    fun getFirstRandomSinger(): String {
        val list = Constants.listOFSingersPart1
        val randomIndex = Random().nextInt(list.size)
        return list.get(randomIndex)
    }

    fun getSecondRandomSinger(): String {
        val list = Constants.listOFSingerspart2
        val randomIndex = Random().nextInt(list.size)
        return list.get(randomIndex)
    }

    fun getRandomApiKey(): String {
        val stringArray = resources.getStringArray(R.array.api_keys)
        return stringArray.get(Random().nextInt(stringArray.size))
    }

    companion object {
        var playListPreviewFragmen = PlayListPreviewFragment()
    }


    override fun onItemClick(item: Item) {
        playListPreviewFragmen = PlayListPreviewFragment()
            .newInstance(item.id.playlistId)
        MainActivity.isFromPlayListPreview = true
        setFragment(playListPreviewFragmen, Constants.PLAYLIST_PREVIEW_FRAGMENT_TAG, 1)
    }

    private fun setFragment(homeFragment: Fragment, tag: String, i: Int) {
        val fm = requireActivity().supportFragmentManager
        val view =
            requireActivity().findViewById<FragmentContainerView>(R.id.MainActiviyt_nav_host_fragment)
        fm.beginTransaction().add(view.id, homeFragment, tag).commit();
        (activity as MainActivity).activeFragment = homeFragment;
    }



}