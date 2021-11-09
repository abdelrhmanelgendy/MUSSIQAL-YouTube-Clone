package com.example.musiqal.fragments

import ItemsInPlayListAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.musiqal.R
import com.example.musiqal.databinding.FragmentPlayListPreviewBinding
import com.example.musiqal.models.youtubeItemInList.Item
import com.example.musiqal.viewModels.MainViewModel
import com.example.musiqal.viewModels.viewStates.VideosInPlayListViewState
import kotlinx.coroutines.flow.collect
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musiqal.fragments.util.OnPlayListPreviewRecyclerViewListener
import com.example.musiqal.models.youtubeItemInList.ItemInPlayListPreview
import com.example.musiqal.models.youtubeItemInList.YoutubeItemsConverters
import com.example.musiqal.ui.MainActivity
import com.example.musiqal.userPLaylists.model.UserPlayList
import com.example.musiqal.userPLaylists.mvi.UserPlayListViewModel
import com.example.musiqal.util.OnAudioInPlaylistClickListner
import com.example.musiqal.util.OnItemVideoInPlayListClickListner
import java.lang.Exception


class PlayListPreviewFragment : Fragment(), OnItemVideoInPlayListClickListner,
    OnPlayListPreviewRecyclerViewListener {
    private var currentPlayList: List<Item> = listOf()
    lateinit var onAudioInPlaylistClickListner: OnAudioInPlaylistClickListner
    private val PLAY_LIST_ID = "playListId"
    val listOfReadyUrls =
        listOf(
            "https://cdn01.ytjar.xyz/get.php/b/86/foE1mO2yM04.mp3?h=TTJ6EVhZ0cKJHIQw14AQtg&s=1635199942&n=Mike-Posner-I-Took-A-Pill-In-Ibiza-Seeb-Remix-Explicit",
            "https://cdn01.ytjar.xyz/get.php/2/ae/J9NQFACZYEU.mp3?h=bOzK5hSFDhVqFiZFDK07qA&s=1635200055&n=Calvin-Harris-Outside-Official-Video-ft-Ellie-Goulding",
            "https://cdn02.ytjar.xyz/get.php/8/9e/RnBT9uUYb1w.mp3?h=AF3BZNpCRLkZrw65SfzSCQ&s=1635200090&n=Martin-Garrix-Bebe-Rexha-In-The-Name-Of-Love-Official-Video",
            "https://cdn01.ytjar.xyz/get.php/3/1a/FrG4TEcSuRg.mp3?h=NMCnKIV8CiMa4sGQNWjIPA&s=1635200107&n=PSY-DADDY-feat-CL-of-2NE1-M-V",
            "https://cdn01.ytjar.xyz/get.php/d/8f/b4Bj7Zb-YD4.mp3?h=hIzbcvynMojsFVli8miSvQ&s=1635200129&n=Calvin-Harris-My-Way-Official-Video"
        )


    fun newInstance(param1: String) =
        PlayListPreviewFragment().apply {
            arguments = Bundle().apply {
                putString(PLAY_LIST_ID, param1)
            }
        }


    private val ITEM_LIST_INSHARED: String = "sharedSavedItem"
    lateinit var binding: FragmentPlayListPreviewBinding
    val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }
    val TAG = "PlayListPreviewTAG"
    val TAG2 = "convertVideoToUrl"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")
        binding = FragmentPlayListPreviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.isFromPlayListPreview = true

        val fromShared = true
        if (fromShared) {
            val itemFromSharedPreference = getItemFromSharedPreference()
            val listOfItem = YoutubeItemsConverters().convertItems(itemFromSharedPreference)
            Log.d(TAG, "onViewCreated: ${listOfItem.get(0).toString()}")
            Log.d(TAG, "onViewCreated: ${listOfItem.get(1).toString()}")
            setupRecyclerViewData(listOfItem)

        } else {
            getDataByListID(arguments?.getString(PLAY_LIST_ID)!!)
        }
//
//        activity?.findViewById<BottomNavigationView>(R.id.mainActivity_bottomNavigation)?.visibility =
//            View.GONE
//        findNavController().navigate(R.id.action_playListPreviewFragment_to_homeFragment)
        initButtons()
    }

    private fun initButtons() {
        binding.playListPreviewFragmentImageAddCurrentPlayList.setOnClickListener {
            saveCurrentPLayList(this.currentPlayList)

        }
        binding.playListPreviewFragmentBtnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    private fun saveCurrentPLayList(currentPlayList: List<Item>) {
        val userPlayListViewModel=ViewModelProvider(requireActivity())
            .get(UserPlayListViewModel::class.java)




    }

    lateinit var itemsInPlayListAdapter: ItemsInPlayListAdapter
    fun setupRecyclerViewData(items: List<Item>) {
        this.currentPlayList = items
        val listOfSelectableItem: ArrayList<ItemInPlayListPreview> =
            convertItemToSelectableItem(items)
        itemsInPlayListAdapter = ItemsInPlayListAdapter(requireContext(), this)
        val layoutManager = LinearLayoutManager(requireContext())

        binding.playListPreviewFragmentRecyclerView.also {
            it.layoutManager = layoutManager
            it.adapter = itemsInPlayListAdapter
            itemsInPlayListAdapter.setListOfItems(listOfSelectableItem, 0)
        }
    }

    private fun convertItemToSelectableItem(items: List<Item>): ArrayList<ItemInPlayListPreview> {
        val arrayListOfSelectableItems = arrayListOf<ItemInPlayListPreview>()
        for (singleItem in items) {
            arrayListOfSelectableItems.add(ItemInPlayListPreview(singleItem, false))
        }
        return arrayListOfSelectableItems
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val playListId = it.getString(PLAY_LIST_ID)
            Log.d(TAG, "onCreate: recieved PlayList " + playListId)

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

    private fun getDataByListID(playListID: String) {

        mainViewModel.getVideosInsidePlaylist(
            "snippet",
            playListID,
            "100",
            resources.getStringArray(R.array.api_keys).get(4)
        )

        lifecycleScope.launchWhenStarted {
            mainViewModel.itemInPlayLitsStateFlow.collect { event ->
                when (event) {
                    is VideosInPlayListViewState.Failed -> {
                        Log.d(TAG, "getDataByListID: ")
                        getDataByListID(playListID)
                    }
                    is VideosInPlayListViewState.Success -> {
                        setUpData(event.youtubeVideosInPlaylistRequest.items)
                    }
                    is VideosInPlayListViewState.Loading -> {
                        Log.d(TAG, "getDataByListID: Loading")
                    }


                }
            }
        }

    }

    private fun setFragment(tag: String, i: Int) {
        val fm = requireActivity().supportFragmentManager
        fm.beginTransaction().hide(HomeFragment.playListPreviewFragmen).commit();

    }

    private fun setUpData(items: List<Item>) {
        savePlayListInSharesPref(items)


    }

    fun savePlayListInSharesPref(items: List<Item>) {

        val sharedPreferences =
            requireContext().getSharedPreferences("playListPreview", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(ITEM_LIST_INSHARED, YoutubeItemsConverters().convertToString(items)).apply()
        Log.d(TAG, "savePlayListInSharesPref: Saved")


    }

    fun getItemFromSharedPreference(): String {
        val sharedPreferences =
            requireContext().getSharedPreferences("playListPreview", Context.MODE_PRIVATE)
        return sharedPreferences.getString(ITEM_LIST_INSHARED, "-1")!!


    }

    override fun onVideoClick(
        item: Item,
        ids: List<String>,
        _listOfYoutubeItemsInPlaylists: MutableList<Item>,
        position: Int,
        _listOfSelectableYoutubeItemsInPLayListPreview: MutableList<ItemInPlayListPreview>
    ) {

        onAudioInPlaylistClickListner.onItemClick(
            item,
            _listOfYoutubeItemsInPlaylists,
            position,
            item.snippet.channelTitle
        )

        changeNowPlayingGifPosition(_listOfSelectableYoutubeItemsInPLayListPreview, position)
//        val videoId = item.snippet.resourceId.videoId
//        getVideoLink(Constants.MP3_API_ALL_API_KEY.get(1), videoId)

    }

    private fun changeNowPlayingGifPosition(
        _listOfSelectableYoutubeItemsInPLayListPreview: MutableList<ItemInPlayListPreview>,
        currentPosition: Int
    ) {

        _listOfSelectableYoutubeItemsInPLayListPreview.forEach { selectableItem ->
            selectableItem.isSelected = false
        }
        _listOfSelectableYoutubeItemsInPLayListPreview.get(currentPosition).isSelected = true
        itemsInPlayListAdapter.setListOfItems(
            _listOfSelectableYoutubeItemsInPLayListPreview,
            currentPosition
        )
    }



    private fun changeApiKey(rapidApiKey: String): String {

        return ""
    }

    override fun onScroll(position: Int, currentListOfItems: List<Item>) {


        if (::binding.isInitialized) {
            binding.playListPreviewFragmentRecyclerView.scrollToPosition(position)
            val convertItemToSelectableItem = convertItemToSelectableItem(currentListOfItems)
            changeNowPlayingGifPosition(convertItemToSelectableItem, position)
        }
    }


}
