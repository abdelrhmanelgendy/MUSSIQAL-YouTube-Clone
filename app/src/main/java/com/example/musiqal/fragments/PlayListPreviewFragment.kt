package com.example.musiqal.fragments

import ItemsInPlayListAdapter
import android.content.Context
import android.graphics.Color
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
import com.example.musiqal.util.OnAudioInPlaylistClickListner
import com.example.musiqal.util.OnItemVideoInPlayListClickListner
import java.lang.Exception


const val PLAY_LIST_ID = "playListId"

class PlayListPreviewFragment : Fragment(), OnItemVideoInPlayListClickListner,
    OnPlayListPreviewRecyclerViewListener {
    lateinit var onAudioInPlaylistClickListner: OnAudioInPlaylistClickListner
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
    lateinit var playListPreviewBinding: FragmentPlayListPreviewBinding
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
        playListPreviewBinding = FragmentPlayListPreviewBinding.inflate(layoutInflater)
        return playListPreviewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.isFromPlayListPreview = true

        val fromShared = true
        if (fromShared) {
            val itemFromSharedPreference = getItemFromSharedPreference()
            val listOfItem = YoutubeItemsConverters().convertItems(itemFromSharedPreference)
            Log.d(TAG, "onViewCreated: ${listOfItem.toString()}")
            setupRecyclerViewData(listOfItem)

        } else {
            getDataByListID(arguments?.getString(PLAY_LIST_ID)!!)
        }
//
//        activity?.findViewById<BottomNavigationView>(R.id.mainActivity_bottomNavigation)?.visibility =
//            View.GONE
//        findNavController().navigate(R.id.action_playListPreviewFragment_to_homeFragment)

    }

    lateinit var itemsInPlayListAdapter: ItemsInPlayListAdapter
    fun setupRecyclerViewData(items: List<Item>) {
        val listOfSelectableItem: ArrayList<ItemInPlayListPreview> =
            convertItemToSelectableItem(items)
        itemsInPlayListAdapter = ItemsInPlayListAdapter(requireContext(), this)
        val layoutManager = LinearLayoutManager(requireContext())

        playListPreviewBinding.playListPreviewFragmentRecyclerView.also {
            it.layoutManager = layoutManager
            it.adapter = itemsInPlayListAdapter
            itemsInPlayListAdapter.setListOfItems(listOfSelectableItem,0)
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
        itemsInPlayListAdapter.setListOfItems(_listOfSelectableYoutubeItemsInPLayListPreview,currentPosition    )
    }


    fun getVideoLink(rapidApiKey: String, videoId: String) {

//        val dommyUrl = listOfReadyUrls.get(java.util.Random().nextInt(listOfReadyUrls.size - 1))
//        onAudioInPlaylistClickListner.onAudioExtractedClick(
//            YoutubeMp3ConverterData(
//                0.0,
//                dommyUrl,
//                "",
//                1,
//                "",
//                ""
//            )
//        )
//        mainViewModel.getMp3VideoConvertedUrl(
//            "youtube-mp36.p.rapidapi.com",
//            rapidApiKey,
//            videoId
//        )
//        lifecycleScope.launchWhenStarted {
//            mainViewModel.youtubeVideoToMp3StateFlow.collect { event ->
//                when (event) {
//                    is YoutubeVideoToMp3StateFlow.Failed -> {
//                        Log.d(TAG2, "video Failed: " + event.errorMessgae)
//                        if (event.errorMessgae.equals(Constants.mp3ApiTooManyRequestsError) || event.errorMessgae.equals(
//                                "Unauthorized"
//                            )
//                        ) {
//                            Log.d(TAG2, "getVideoLink: Changing ApiKey")
//                            val newApiKey = changeApiKey(rapidApiKey)
//                            getVideoLink(newApiKey, videoId)
//                        } else {
//                            Log.d(TAG2, "getVideoLink: reCallMethod")
//                            getVideoLink(rapidApiKey, videoId)
//
//                        }
//
//                    }
//                    is YoutubeVideoToMp3StateFlow.Success -> {
////                        if (event.item.progress)
//                        Log.d(TAG2, "onVideoClick: Video Success " + event.item)
//                        onAudioInPlaylistClickListner.onAudioExtractedClick(event.item)
//
//
//                    }
//                    is YoutubeVideoToMp3StateFlow.Loading -> {
//                        Log.d(TAG2, "onVideoClick: Loading")
//                    }
//
//
//                }
//            }
//        }
    }

    private fun changeApiKey(rapidApiKey: String): String {

        return ""
    }

    override fun onScroll(position: Int, currentListOfItems: List<Item>) {


      if (::playListPreviewBinding.isInitialized)
      {
          playListPreviewBinding.playListPreviewFragmentRecyclerView.scrollToPosition(position)
          val convertItemToSelectableItem = convertItemToSelectableItem(currentListOfItems)
          changeNowPlayingGifPosition(convertItemToSelectableItem, position)
      }
    }



}
