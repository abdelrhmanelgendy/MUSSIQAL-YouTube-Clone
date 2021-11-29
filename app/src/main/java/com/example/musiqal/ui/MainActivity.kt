package com.example.musiqal.ui

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.musiqal.R
import dagger.hilt.android.AndroidEntryPoint
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.musiqal.datamodels.youtube.converter.toaudio.YoutubeMp3ConverterData
import com.example.musiqal.datamodels.youtubeItemInList.Item
import com.example.musiqal.util.*
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.media.MediaPlayer
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.musiqal.customeMusicPlayer.*
import com.example.musiqal.customeMusicPlayer.data.MusicPlayerPersistence
import com.example.musiqal.customeMusicPlayer.musicNotification.MusicNotificationReceiverListener
import com.example.musiqal.customeMusicPlayer.musicNotification.NotificationHelper
import com.example.musiqal.customeMusicPlayer.musicNotification.NotificationReciever
import com.example.musiqal.customeMusicPlayer.musicNotification.NotificationService
import com.example.musiqal.customeMusicPlayer.musicNotification.model.LastPlayedSongData
import com.example.musiqal.customeMusicPlayer.musicNotification.model.LastPlayedTrack
import com.example.musiqal.customeMusicPlayer.util.MusicPlayerEvents
import com.example.musiqal.customeMusicPlayer.util.OnCustomeMusicPlayerCompletionListener
import com.example.musiqal.customeMusicPlayer.util.RepeateMode
import com.example.musiqal.customeMusicPlayer.util.ShuffleMode
import com.example.musiqal.databinding.ActivityMainBinding
import com.example.musiqal.extractVideoDirectLink.VideoLinkToDirectUrlExtraction
import com.example.musiqal.extractVideoDirectLink.util.VideoExtractionEventListener
import com.example.musiqal.fragments.*
import com.example.musiqal.fragments.util.OnCollectionFragmentListeners
import com.example.musiqal.lyrics.LyricsBottomSheet
import com.example.musiqal.lyrics.LyricsUtil
import com.example.musiqal.lyrics.util.OnLyricsFoundListener
import com.example.musiqal.datamodels.youtubeItemInList.ItemTypeConverter
import com.example.musiqal.downloadManager.data.DownloadInfo
import com.example.musiqal.downloadManager.util.DownloadTrack
import com.example.musiqal.search.SearchActivity.Companion.SEARCH_TITLE_KEY
import com.example.musiqal.ui.slidingPan.SlidingUpDownPanel
import com.example.musiqal.userPLaylists.dialogs.UserPLaylistsDialog
import com.example.musiqal.util.MusicPlayerViewPagerAdapter
import com.example.musiqal.viewModels.MainViewModel
import com.example.musiqal.youtubeAudioVideoExtractor.YouTubeDurationConverter

import com.example.musiqal.youtubeAudioVideoExtractor.mvi.YouTubeExtractorViewModel
import kotlinx.coroutines.*
import okhttp3.*
import java.lang.Exception
import java.util.*
import kotlin.random.Random


@AndroidEntryPoint
class MainActivity() :
    AppCompatActivity(),
    OnAudioInPlaylistClickListner,
    OnCustomeMusicPlayerCompletionListener,
    MusicPlayerEvents,
    View.OnClickListener,
    OnCollectionFragmentListeners,
    MusicNotificationReceiverListener, VideoExtractionEventListener {
    private var CurrentItemDuration: String = "-1"
    private lateinit var OUTSIDE_FRAGMENT: RecentlyPlayedTracksFragment
    private val TAG = "MainActivity11"


    val listOfReadyUrls =
        listOf(
            "https://cdns-preview-0.dzcdn.net/stream/c-0b5d54798687583efbb35a42f3ca7bed-6.mp3",
            "https://cdns-preview-6.dzcdn.net/stream/c-671b7c69e2c36d6ba666ade79b9b84de-6.mp3",

            )

    lateinit var binding: ActivityMainBinding
    val ALLOWED_AUDIO_TIME_IN_SECCONDS = 400
    val libraryFragment = LibraryFragment()
    val searchResultFragment = SearchResultFragment()
    val collectionFragment = CollectionsFragment()
    lateinit var motionLayout: MotionLayout
    lateinit var shuffleMode: ShuffleMode
    lateinit var savedRepeateMode: RepeateMode
    lateinit var customeMusicPlayer: CustomeMusicPlayback
    
    lateinit var playListName: String
    lateinit var notificationHelper: NotificationHelper

    val youTubeExtractorViewModel: YouTubeExtractorViewModel by lazy {
        ViewModelProvider(this).get(YouTubeExtractorViewModel::class.java)
    }


    var currentItemPosion: Int = -1
    var isAudioCompleted = false
    val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }


    lateinit var musicPlayerPersistence: MusicPlayerPersistence

    companion object {
        var homeFragment = HomeFragment()
        var isFromPlayListPreview = false
        var isPlaying = true
        var isFromStaticPlayer = false
        lateinit var currentItem: Item
        lateinit var currentListOfItems: List<Item>
    }

    var activeFragment = Fragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFragmentF(homeFragment)


        musicPlayerPersistence = MusicPlayerPersistence(this)
        getMusicPlayerSavedSettings(musicPlayerPersistence)

        setUpMotionLayout()
        initializeCustomeMusicPlayer()
        initializeBottomNavigationView()


//        getLastPlayedSongData()

        initializeViewsListeners()
        setPanelSliderListener(binding.mainActiviytSlidingLayout)
        initNotificationReciver()
        binding.mediaPlayerItemImgLyrics.setOnClickListener {
            openLyricsView()
        }

        binding.mediaPlayerItemImgAddFavorite.setOnClickListener {
            openAddingToPlaylistDialog()
        }
        binding.mediaPlayerItemImgDownload.setOnClickListener { 
            downLoadCurrentItem(currentItem)
        }

//        mainViewModel.deleteAllSavedTrackItem()
    }

    private fun downLoadCurrentItem(currentItem: Item) {
        val trackTitle=currentItem.snippet.title
        val trackDuration="PT11H54M48S"
        val trackId=currentItem.snippet.resourceId.videoId
        Log.d(TAG, "downLoadCurrentItem: "+trackDuration+" "+trackTitle+" "+trackId)
        DownloadTrack(this, DownloadInfo(trackTitle,trackDuration,trackId))

    }

    private fun openAddingToPlaylistDialog() {
        val userPLaylistsDialog = UserPLaylistsDialog(this)
        userPLaylistsDialog.createDialog(currentItem)

    }

    var isViewed = false

    private fun openLyricsView() {
        isViewed = true
        Log.d(TAG, "openLyricsView: ")
        val lyricsUtil = LyricsUtil(this, object : OnLyricsFoundListener {
            override fun onSuccess(lyrics: String) {
                if (isViewed) {

                    Log.d(TAG, "onSuccess:12121 ")
                    val lyricsBottomSheet = LyricsBottomSheet.newInstance(lyricsFilter(lyrics))
                    lyricsBottomSheet.show(supportFragmentManager, "lyricsBottomSheet")
                    isViewed = false
                }


            }

            override fun onFailed(error: String) {
                Log.d(TAG, "onFailed: " + error)
                Toast.makeText(
                    this@MainActivity,
                    "we can't find your lyrics now,\nwe will consider this problem as soon as possible",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        lyricsUtil.initialize(currentItem.snippet.resourceId.videoId, currentItem.snippet.title)
    }

    private fun lyricsFilter(lyrics: String): String {
        val Lyrics_CopyRight = "Paroles de la chanson"

        return lyrics.replace(Lyrics_CopyRight, "")


    }


    private fun getLastPlayedSongData() {

        val musicPlayerServiseState =
            ServiceState().isMyServiceRunning(NotificationService::class.java, this)
        if (musicPlayerServiseState) {
            try {
                initializeLastPlayingDataFromStaticMediaPlayer()

            } catch (e: Exception) {

            }
        } else {
            val lastPlayedList = musicPlayerPersistence.getLastPlayedList()
            val lastPlayedSong = musicPlayerPersistence.getLastPlayedSong()
            val lastPlayedTrack = musicPlayerPersistence.getLastPlayedTrack()
            initializeLastPlayingDataFromSharedPreference(
                lastPlayedList,
                lastPlayedSong,
                lastPlayedTrack
            )
        }

    }

    private fun initializeLastPlayingDataFromStaticMediaPlayer() {
        val CurrentItem = CustomeMusicPlayback.songItem
        val currentPlayList = CustomeMusicPlayback.songPlayList
        val currentPosition = CustomeMusicPlayback.songItemPosition
        val currentPlaylistName = CustomeMusicPlayback.currentPlayListName

        if (customeMusicPlayer.isMediaPlayerPlaying) {
            Log.d(TAG, "playPauseAudio: playing")


        } else {
            Log.d(TAG, "playPauseAudio: not playing")
            isFromStaticPlayer = true
            customeMusicPlayer.stop()


        }
        this.playListName = currentPlaylistName
        currentItemPosion = currentPosition
        currentListOfItems = currentPlayList
        currentItem = CurrentItem
        setupMainMediaPlayerViews()
        setUpViewPagersView(currentPlayList.toMutableList(), currentPosition)

        setUpMainViewsOfTheCurrentPlayingService(CustomeMusicPlayback.songItem)
    }

    private fun initializeLastPlayingDataFromSharedPreference(
        lastPlayedList: List<Item>,
        lastPlayedSong: LastPlayedSongData,
        lastPlayedTrack: LastPlayedTrack
    ) {

        val lastSavedTrackId = lastPlayedTrack.trackId
        val lastSavedItemTrackId = lastPlayedSong.item.snippet.resourceId.videoId

        if (lastSavedTrackId.equals(lastSavedItemTrackId)) {
            currentItemPosion = lastPlayedSong.itemPosition
            currentListOfItems = lastPlayedList.toMutableList()
            currentItem = lastPlayedSong.item
            this.playListName = "lastPlayedList"
            setupMainMediaPlayerViews()
            setUpViewPagersView(lastPlayedList.toMutableList(), lastPlayedSong.itemPosition)
            getVideoLinkWithSeeking(
                "",
                lastPlayedSong.item.snippet.resourceId.videoId,
                lastPlayedTrack.currentDuration
            )
            isFromStaticPlayer = true


        }


    }

    private fun setUpMainViewsOfTheCurrentPlayingService(
        songItem: Item
    ) {
        customeMusicPlayer.setUpViews(songItem, playListName)
        if (isPlaying) {
            binding.mediaPlayerItemImgPlayPauseButtom.setImageResource(R.drawable.ic_baseline_pause_24)
        } else {
            binding.mediaPlayerItemImgPlayPauseButtom.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }

    private fun initNotificationReciver() {
        NotificationReciever.musicNotificationReceiver = this
    }

    private fun initializeViewsListeners() {
        binding.mediaPlayerItemImgPlayPauseButtom.setOnClickListener(this)
        binding.mediaPlayerItemImgNext.setOnClickListener(this)
        binding.mediaPlayerItemImgPrevious.setOnClickListener(this)
        binding.mediaPlayerItemImgShuffle.setOnClickListener(this)
        binding.mediaPlayerItemImgPlayRepeatMode.setOnClickListener(this)
        binding.mediaPlayerItemImgAddFavorite.setOnClickListener(this)
        binding.mediaPlayerItemImgDownload.setOnClickListener(this)
        binding.mediaPlayerItemTVSongName.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            binding.mediaPlayerItemImgPlayPauseButtom.id -> playPauseAudio()
            binding.mediaPlayerItemImgNext.id -> {
                extractNextAudioUrl()
//                CustomeMusicPlayback.fromSharedPreference = false
            }
            binding.mediaPlayerItemImgPrevious.id -> {
                extractPreviousAudioUrl()
//                CustomeMusicPlayback.fromSharedPreference = false
            }
            binding.mediaPlayerItemImgShuffle.id -> shuffleClicked()
            binding.mediaPlayerItemImgPlayRepeatMode.id -> repeatePlayModeClicked()
            binding.mediaPlayerItemImgAddFavorite.id -> addFavorite()
            binding.mediaPlayerItemImgDownload.id -> downLoadCurrentAudio()
            binding.mediaPlayerItemTVSongName.id -> openPanelLayout()
        }

    }

    fun shuffleClicked() {
        if (shuffleMode == ShuffleMode.Shuffle) {
            binding.mediaPlayerItemImgShuffle.setImageResource(R.drawable.ic_baseline_no_shuffle_24)
            musicPlayerPersistence.saveShuffleMode(ShuffleMode.NoShuffle)
            shuffleMode = ShuffleMode.NoShuffle
            Log.d(TAG, "shuffle: ")
        } else if (shuffleMode == ShuffleMode.NoShuffle) {
            binding.mediaPlayerItemImgShuffle.setImageResource(R.drawable.ic_baseline_shuffle_24)
            musicPlayerPersistence.saveShuffleMode(ShuffleMode.Shuffle)
            shuffleMode = ShuffleMode.Shuffle
            Log.d(TAG, "No shuffle: ")
        }

    }


    private fun initializeCustomeMusicPlayer() {


        if (!::customeMusicPlayer.isInitialized) {
            customeMusicPlayer =
                mainViewModel.provideSingleMediaPlayerInstance()
            customeMusicPlayer.onCustomeMusicPlayerCompletionListener = this
            customeMusicPlayer.setViews(
                binding.mediaPlayerItemTVTxtPlayedTimeInMinutes,
                binding.mediaPlayerItemTVTxtRemainingTimeInMinutes,
                binding.mediaPlayerItemMediaPlayerSeekBar,
                binding.mediaPlayerItemImgPlayPauseButtom
            )
        }


    }

    private fun setUpMotionLayout() {
        motionLayout = binding.mainActiviytMotionLayout
        motionLayout.transitionToEnd()
    }

    private fun setPanelSliderListener(slidingUpDownPanel: SlidingUpDownPanel) {
        slidingUpDownPanel.setOnPanelSlideListener(object :
            SlidingUpDownPanel.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                Log.d(
                    TAG,
                    "onPanelSlide: " + binding.mainActivityBottomNavigation.height
                )

                if (slideOffset >= 0 && slideOffset <= 1) {
                    motionLayout.progress = (1 - slideOffset)
                    Log.d(TAG, "onPanelSlide: " + slideOffset)
                }
                binding.mainActivityBottomNavigation.alpha = 1 - slideOffset


                if (slideOffset < .99999) {
                    binding.MainActiviytCoordinatorLayout.setBackgroundColor(
                        Color.BLACK
                    )
                    binding.mediaPlayerItemTVNowPlayText.isVisible = false

                } else {

                    if (::viewsColorEvaluter.isInitialized) {
                        binding.MainActiviytCoordinatorLayout.setBackgroundColor(
                            viewsColorEvaluter.evaluatedColor
                        )
                    }
                    binding.mediaPlayerItemTVNowPlayText.isVisible = true

                }


            }

            override fun onPanelCollapsed(panel: View?) {

            }

            override fun onPanelExpanded(panel: View?) {

            }

            override fun onPanelAnchored(panel: View?) {
            }

            override fun onPanelHidden(panel: View?) {
            }
        })
    }

    /**
     *getting last played music
     *getting last played music duration
     *getting shuffle mode
     *getting repeate mode
     */

    /**
     *getting last played music
     *getting last played music duration
     *getting shuffle mode
     *getting repeate mode
     */
    private fun getMusicPlayerSavedSettings(musicPlayerPersistence: MusicPlayerPersistence) {
        val repeatMode = musicPlayerPersistence.getRepeatMode()
        savedRepeateMode = repeatMode
        setUpRepeatModeButton(repeatMode)
        val savedshuffleMode = musicPlayerPersistence.getShuffleMode()
        shuffleMode = savedshuffleMode
        setUpShuffleModeButton(shuffleMode)
    }


    private fun setUpRepeatModeButton(repeatMode: RepeateMode) {
        Log.d(TAG, "setUpRepeatModeButton: " + repeatMode.repeatMode)
        when (repeatMode) {
            is RepeateMode.RepeateAll -> binding.mediaPlayerItemImgPlayRepeatMode.setImageResource(
                R.drawable.ic_baseline_repeat_all
            )
            is RepeateMode.RepeateOnc -> binding.mediaPlayerItemImgPlayRepeatMode.setImageResource(
                R.drawable.ic_baseline_repeat_one_24
            )
            is RepeateMode.NoRepeating -> binding.mediaPlayerItemImgPlayRepeatMode.setImageResource(
                R.drawable.ic_baseline_no_repeat_all
            )
        }

    }

    private fun setUpShuffleModeButton(shuffleMode: ShuffleMode) {
        Log.d(TAG, "setUpShuffleModeButton: " + shuffleMode.shuffleMode)
        when (shuffleMode) {
            is ShuffleMode.Shuffle -> binding.mediaPlayerItemImgShuffle.setImageResource(
                R.drawable.ic_baseline_shuffle_24
            )
            is ShuffleMode.NoShuffle -> binding.mediaPlayerItemImgShuffle.setImageResource(
                R.drawable.ic_baseline_no_shuffle_24
            )
        }

    }


    private fun openPanelLayout() {
        binding.mainActiviytSlidingLayout.expandPanel()
    }

    private fun extractPreviousAudioUrl() {
        isPlaying = true
        var audioPosition: Int? = null
        if (currentItemPosion == 0) {

        } else {
            if (shuffleMode == ShuffleMode.NoShuffle) {
                audioPosition = currentItemPosion - 1
                binding.mediaPlayerItemViewPagerSongImage.setCurrentItem(
                    audioPosition,
                    true
                )
                val youtubeItem = currentListOfItems.get(audioPosition)
                currentItem = currentListOfItems.get(audioPosition)
                currentItemPosion = audioPosition
                setupMainMediaPlayerViews()
                val videoId = youtubeItem.snippet.resourceId.videoId
            } else if (shuffleMode == ShuffleMode.Shuffle) {
                audioPosition = Random.nextInt(currentListOfItems.size - 1)
                binding.mediaPlayerItemViewPagerSongImage.setCurrentItem(
                    audioPosition,
                    false
                )
                val youtubeItem = currentListOfItems.get(audioPosition)
                currentItem = currentListOfItems.get(audioPosition)
                currentItemPosion = audioPosition
                setupMainMediaPlayerViews()
                val videoId = youtubeItem.snippet.resourceId.videoId

            }

        }

    }

    private fun extractNextAudioUrl() {
        isPlaying = true
        var audioPosition: Int? = null
        if (currentItemPosion == currentListOfItems.size - 1) {


        } else {

            if (shuffleMode == ShuffleMode.NoShuffle) {
                audioPosition = currentItemPosion + 1
                Log.d(TAG, "urlExtracted At position : " + currentItemPosion)

                val youtubeItem = currentListOfItems.get(audioPosition)
                currentItem = currentListOfItems.get(audioPosition)
                currentItemPosion = audioPosition
                setupMainMediaPlayerViews()
                binding.mediaPlayerItemViewPagerSongImage.setCurrentItem(
                    audioPosition,
                    true
                )
                val videoId = youtubeItem.snippet.resourceId.videoId
                //            getVideoLink(Constants.MP3_API_ALL_API_KEY.get(0), videoId)
            } else if (shuffleMode == ShuffleMode.Shuffle) {
                audioPosition = Random.nextInt(currentListOfItems.size - 1)
                Log.d(TAG, "urlExtracted At position : " + currentItemPosion)

                val youtubeItem = currentListOfItems.get(audioPosition)
                currentItem = currentListOfItems.get(audioPosition)
                currentItemPosion = audioPosition
                setupMainMediaPlayerViews()
                binding.mediaPlayerItemViewPagerSongImage.setCurrentItem(
                    audioPosition,
                    false
                )
                val videoId = youtubeItem.snippet.resourceId.videoId
                //            getVideoLink(Constants.MP3_API_ALL_API_KEY.get(0), videoId)
            }

        }


    }

    private fun changeApiKey(rapidApiKey: String): String {
        return ""
    }


    private fun playPauseAudio() {


        if (isFromStaticPlayer) {
            isPlaying = false
            isFromStaticPlayer = false
        }
        Log.d(TAG, "playPauseAudio: " + isPlaying)
        isPlaying = !isPlaying
        if (isPlaying) {
            //playing
            resume()
            Log.d(TAG, "playPauseAudio: resume")


        } else {
            //pausing
            pause()
            Log.d(TAG, "playPauseAudio: pause")
        }

    }


    private fun downLoadCurrentAudio() {}


    override fun onBackPressed() {
        Log.d(
            TAG,
            "onBackPressed: " + currentSettedFragment?.javaClass?.name
        )
        if (currentSettedFragment?.javaClass?.name.equals(UserPlayListPreviewFragment::class.java.name)) {
            val fm = supportFragmentManager
            fm.beginTransaction()
                .hide(currentSettedFragment!!)
                .show(collectionFragment).commit();
            currentSettedFragment = collectionFragment
            return
//            val fm = supportFragmentManager
//
//            fm.beginTransaction()
//                .hide(activeFragment)
//                .show(collectionFragment).commit();
        }
        if (IS_FROM_COLLECTIONFRAGMENT) {
            val fm = supportFragmentManager
            fm.beginTransaction()
                .hide(activeFragment)
                .show(collectionFragment).commit();
            currentSettedFragment = collectionFragment
            IS_FROM_COLLECTIONFRAGMENT = false
            return
        }
        if (currentSettedFragment?.javaClass?.name.equals(SearchResultFragment::class.java.name)) {
            val fm = supportFragmentManager
            fm.beginTransaction()
                .hide(currentSettedFragment!!)
                .show(homeFragment).commit();
            currentSettedFragment = homeFragment
            return
        }
        if (binding.mainActiviytSlidingLayout.isPanelExpanded) {
            motionLayout.transitionToEnd()
            binding.mainActiviytSlidingLayout.collapsePanel()
            return
        }

        val bottomNavigationSelectedItem =
            binding.mainActivityBottomNavigation.selectedItemId
        when (bottomNavigationSelectedItem) {
            (R.id.homeFragment) -> {
                if (!isFromPlayListPreview) {
                    finishAffinity()
                } else {
                    val fm = supportFragmentManager
                    fm.beginTransaction()
                        .hide(HomeFragment.playListPreviewFragmen)
                        .show(homeFragment).commit();
                    isFromPlayListPreview = false
                    currentSettedFragment = homeFragment
                }
            }
            (R.id.libraryFragment) -> {
                setFragment(homeFragment, "0", 1)
                binding.mainActivityBottomNavigation.selectedItemId =
                    R.id.homeFragment
                currentSettedFragment = homeFragment
            }
            (R.id.collectionsFragment) -> {
                setFragment(homeFragment, "0", 1)
                binding.mainActivityBottomNavigation.selectedItemId =
                    R.id.homeFragment
                currentSettedFragment = homeFragment
            }
        }

    }

    var currentSettedFragment: Fragment? = null
    fun setFragmentF(currentFragment: Fragment) {
        Log.d(TAG, "setFragmentF: " + currentFragment::class.java.name)
        val fm = supportFragmentManager
        currentSettedFragment = currentFragment
        if (currentFragment.isAdded()) {
            try {
                fm.beginTransaction().hide(homeFragment).commit()
                fm.beginTransaction().hide(libraryFragment).commit()
                fm.beginTransaction().hide(collectionFragment).commit()
                fm.beginTransaction().hide(HomeFragment.playListPreviewFragmen)
                fm.beginTransaction().hide(searchResultFragment).commit()
                fm.beginTransaction().hide(this.OUTSIDE_FRAGMENT).commit()
                currentSettedFragment?.let {
                    fm.beginTransaction().hide(it).commit()
                }

                fm.beginTransaction().show(currentFragment).commit()
            } catch (e: Exception) {
            }
        } else {
            fm.beginTransaction()
                .add(
                    R.id.MainActiviyt_nav_host_fragment,
                    currentFragment,
                    "tag"
                )
                .commit();
        }

    }

    var firstTimeinitializeViewPager = true
    override fun onItemClick(
        item: Item,
        _listOfYoutubeItemsInPlaylists: MutableList<Item>,
        position: Int,
        playListName: String,
        shuffleMode: ShuffleMode
    ) {
        super.onItemClick(item, _listOfYoutubeItemsInPlaylists, position, playListName, shuffleMode)
        this.shuffleMode = shuffleMode
        shuffleClicked()
        setUpShuffleModeButton(this.shuffleMode)

        onItemClick(item, _listOfYoutubeItemsInPlaylists, position, playListName)
    }

    override fun onItemClick(
        item: Item,
        _listOfYoutubeItemsInPlaylists: MutableList<Item>,
        position: Int,
        playListName: String
    ) {
        this.playListName = playListName
        currentItemPosion = position
        currentListOfItems = _listOfYoutubeItemsInPlaylists
        currentItem = item
        setupMainMediaPlayerViews()
        setUpViewPagersView(_listOfYoutubeItemsInPlaylists, position)
        CurrentItemDuration = item.videoDuration
        Log.d(TAG, "onItemClick12121: " + item.videoDuration)
        if (position == 0) {
            firstTimeinitializeViewPager = true
        }
        if (firstTimeinitializeViewPager) {
            Log.d(TAG, "onItemClick: firstTimeinitializeViewPager")
            getVideoLink("", item.snippet.resourceId.videoId, item)
            firstTimeinitializeViewPager = false
        }


    }

    lateinit var viewsColorEvaluter: ViewsColorEvaluter
    private fun setUpViewPagersView(
        _listOfYoutubeItemsInPlaylists: MutableList<Item>,
        clickedPosition: Int,
    ) {
        viewsColorEvaluter = ViewsColorEvaluter(
            applicationContext,
            binding.mediaPlayerItemImgPlayPause,
            binding.mainContainer,
            binding.MainActiviytLinearLayout,
            binding.MainActiviytCoordinatorLayout,
            binding.mainActiviytSlidingLayout
        )
        val musicPlayerViewPagerAdapter = MusicPlayerViewPagerAdapter(
            layoutInflater,
            _listOfYoutubeItemsInPlaylists,
            applicationContext
        )

        binding.mediaPlayerItemViewPagerSongImage.adapter =
            musicPlayerViewPagerAdapter
        binding.mediaPlayerItemViewPagerSongImage.setPadding(70, 0, 70, 0)
        binding.mediaPlayerItemViewPagerSongImage.setCurrentItem(
            clickedPosition,
            true
        )
        binding.mediaPlayerItemViewPagerSongImage.setOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                Log.d(TAG, "onPageScrolled: " + position)

                CoroutineScope(Dispatchers.Default).launch {
                    if (position < _listOfYoutubeItemsInPlaylists.size - 1) {
                        setMainContainerVariantColor(
                            _listOfYoutubeItemsInPlaylists,
                            position,
                            (position + 1),
                            positionOffset
                        )

                    } else if (position == _listOfYoutubeItemsInPlaylists.size - 1) {
                        setMainContainerVariantColor(
                            _listOfYoutubeItemsInPlaylists,
                            position,
                            position,
                            positionOffset
                        )

                    }
                }
            }


            override fun onPageSelected(position: Int) {


                CoroutineScope(Dispatchers.Default).launch {
                    val item = _listOfYoutubeItemsInPlaylists.get(position)
                    currentItemPosion = position
                    currentItem = currentListOfItems.get(currentItemPosion)
                    CurrentItemDuration = currentItem.videoDuration
                    setupMainMediaPlayerViews()
                    val dommyUrl =
                        listOfReadyUrls.get(
                            java.util.Random().nextInt(listOfReadyUrls.size - 1)
                        )
                    val audioItem = YoutubeMp3ConverterData(
                        0.0,
                        dommyUrl,
                        "",
                        1,
                        "",
                        ""
                    )

                    getVideoLink("", item.snippet.resourceId.videoId, item)
                    withContext(Dispatchers.Main) {
                        delay(100)
                        HomeFragment.playListPreviewFragmen
                            .onScroll(currentItemPosion, currentListOfItems)

                    }
                }

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

    }


    private fun setMainContainerVariantColor(
        _listOfYoutubeItemsInPlaylists: MutableList<Item>,
        position: Int,
        nextPosition: Int,
        positionOffset: Float

    ) {
        val item1 = _listOfYoutubeItemsInPlaylists.get(position)
        val item2 = _listOfYoutubeItemsInPlaylists.get(nextPosition)
        viewsColorEvaluter.generateColors(positionOffset, item1, item2)


    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getDefaultBitmab(): Bitmap {
        val drawable = (resources.getDrawable(R.drawable.private_video, theme))!!
        val bitmap = (drawable as BitmapDrawable).bitmap!!
        return bitmap
    }


    fun setupMainMediaPlayerViews() {
        val item = currentItem
        val title = item
            .snippet.title
        val imageUrl = ImageUrlUtil.getMaxResolutionImageUrl(item)
        CoroutineScope(Dispatchers.Main).launch {
            binding.mediaPlayerItemTVSongName.text = title
        }
        Log.d(TAG, "setupMainMediaPlayerViews: " + item.snippet.title)
        if (item.snippet.title.equals(Constants.PRIVATE_VIDEO)) {
            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(applicationContext)
                    .load(R.drawable.private_video)
                    .into(binding.mediaPlayerItemImagSongImage2)
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(applicationContext)
                    .load(imageUrl)
                    .into(binding.mediaPlayerItemImagSongImage2)
            }
        }

    }

    var x = 0
    override fun start(
        videoId: String,
        currentItem: Item,
        currentPlayList: List<Item>,
        currentItemPosition: Int, playListName: String
    ) {

//        stop()

//        checkFile -> video lenth in minutes
        val audioInSeconds = checkFileSize(currentItem)
        if (audioInSeconds <= ALLOWED_AUDIO_TIME_IN_SECCONDS) {

            Log.d(TAG, "start: find for premuim user")
            VideoLinkToDirectUrlExtraction(
                this,
                this,
                VideoLinkToDirectUrlExtraction.PREMUIM_USER_TYPE
            ).getVideoLink(videoId)
        } else {

            //App is not designed for such  a big audio do you want to download it instead of playing it?
            Log.d(TAG, "start: find for Normal user")
            if (audioInSeconds > 600) {
                MakingToast(applicationContext).toast(
                    "Video with more than 10 minutes are slow streaming\nplease be patient.",
                    MakingToast.LENGTH_SHORT
                )
            }
            VideoLinkToDirectUrlExtraction(
                this,
                this,
                VideoLinkToDirectUrlExtraction.NORMAL_USER_TYPE
            ).getVideoLink(videoId)
        }


    }


    private fun checkFileSize(item: Item): Int {
        val timeInSeconds = YouTubeDurationConverter.getTimeInSeconds(item.videoDuration)
        return timeInSeconds

    }

    var isStartPlayingSuccess = false
    override fun onSuccess(directUrl: String) {


        if (!isStartPlayingSuccess) {
            Log.d(TAG, "onSuccess: getting url success  $directUrl")
            isStartPlayingSuccess = true
            CustomeMusicPlayback.songItem = currentItem
            customeMusicPlayer.start(
                directUrl,
                currentItem,
                currentListOfItems,
                currentItemPosion,
                playListName
            )

            savePlayingTrackIntoHistory(currentItem)
        }
    }

    private fun savePlayingTrackIntoHistory(currentItem: Item) {
        currentItem.playedTrackID = null
        currentItem.playedDateInMillisSecond = System.currentTimeMillis().toString()
        mainViewModel.insertSavedTrackItem(currentItem)
        mainViewModel.getAllPLayedTrackHistory()

    }

    private fun startWithSeeking(
        url: String,
        currentItem: Item,
        currentPlayList: List<Item>,
        currentItemPosion: Int,
        playListName: String,
        currentDuration: Long
    ) {
//        customeMusicPlayer.startWithSeeking(
//            url,
//            currentItem,
//            currentPlayList,
//            currentItemPosion,
//            playListName,
//            currentDuration
//        )
    }


    override fun pause() {

        customeMusicPlayer.pause()
        Log.d(TAG, "pause: " + customeMusicPlayer.hashCode())
    }

    override fun resume() {
        if (isAudioCompleted && savedRepeateMode == RepeateMode.NoRepeating) {
            getVideoLink("", currentItem.snippet.resourceId.videoId, currentItem)
            isAudioCompleted = false
            return
        }
        customeMusicPlayer.resume()


    }

    override fun stop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.mediaPlayerItemMediaPlayerSeekBar.setProgress(0, true)
        } else {
            binding.mediaPlayerItemMediaPlayerSeekBar.setProgress(0)
        }

    }

    private fun nextTrackWithSeeking(link: String, currentDuration: Long) {
        startWithSeeking(
            url = link,
            currentItem,
            currentPlayList = currentListOfItems,
            currentItemPosion,
            playListName,
            currentDuration
        )
    }


    override fun next(videoId: String, item: Item) {
        if (item.videoDuration.equals("P0D")) {

            lifecycleScope.launch(Dispatchers.Main) {
                MakingToast(applicationContext).toast(
                    "sorry playing live videos is not avaiblable now",
                    MakingToast.LENGTH_SHORT
                )
            }

        } else {
            isStartPlayingSuccess = false
            customeMusicPlayer.stop()
            customeMusicPlayer.emptyViews()
            start(
                videoId = videoId,
                item,
                currentPlayList = currentListOfItems,
                currentItemPosion,
                playListName
            )
        }
    }

    override fun previouse(url: String) {
        isStartPlayingSuccess = false
        start(
            videoId = url,
            currentItem,
            currentPlayList = currentListOfItems,
            currentItemPosion,
            playListName
        )

        Log.d(TAG, "previouse: " + customeMusicPlayer.hashCode())
    }

    fun repeatePlayModeClicked() {

        if (savedRepeateMode == RepeateMode.NoRepeating) {
            savedRepeateMode = RepeateMode.RepeateOnc
            binding.mediaPlayerItemImgPlayRepeatMode.setImageResource(R.drawable.ic_baseline_repeat_one_24)
            saveRepeatingMode(savedRepeateMode)
        } else if (savedRepeateMode == RepeateMode.RepeateOnc) {
            savedRepeateMode = RepeateMode.RepeateAll
            binding.mediaPlayerItemImgPlayRepeatMode.setImageResource(R.drawable.ic_baseline_repeat_all)
            saveRepeatingMode(savedRepeateMode)
        } else if (savedRepeateMode == RepeateMode.RepeateAll) {
            savedRepeateMode = RepeateMode.NoRepeating
            binding.mediaPlayerItemImgPlayRepeatMode.setImageResource(R.drawable.ic_baseline_no_repeat_all)
            saveRepeatingMode(savedRepeateMode)
        }

    }

    private fun saveRepeatingMode(repeateMode: RepeateMode) {
        musicPlayerPersistence.saveRepeatMode(repeateMode)

    }

    override fun addFavorite() {
    }


    override fun seekTo(newTime: Int) {
        customeMusicPlayer.seekTo(newTime)
    }

    override fun onComplete(mediaPlayer: MediaPlayer) {
        //if no internet the return
        if (savedRepeateMode == RepeateMode.RepeateOnc) {

            binding.mediaPlayerItemViewPagerSongImage.setCurrentItem(
                currentItemPosion,
                true
            )
            getVideoLink(changeApiKey(""), currentItem.snippet.resourceId.videoId, currentItem)


        } else if (savedRepeateMode == RepeateMode.NoRepeating) {
            isAudioCompleted = true
            return

        } else if (savedRepeateMode == RepeateMode.RepeateAll) {

            if (currentItemPosion < (currentListOfItems.size - 1)) {
                if (shuffleMode == ShuffleMode.NoShuffle) {
                    currentItemPosion++
                    binding.mediaPlayerItemViewPagerSongImage.setCurrentItem(
                        currentItemPosion,
                        true
                    )
                } else {
                    currentItemPosion = Random.nextInt(currentListOfItems.size - 1)
                    binding.mediaPlayerItemViewPagerSongImage.setCurrentItem(
                        currentItemPosion,
                        false
                    )
                }

            } else if (currentItemPosion == currentListOfItems.size - 1) {
                if (shuffleMode == ShuffleMode.Shuffle) {
                    currentItemPosion = Random.nextInt(currentListOfItems.size - 1)
                    binding.mediaPlayerItemViewPagerSongImage.setCurrentItem(
                        currentItemPosion,
                        false
                    )
                }
            }
        }


    }

    override fun onSeeking(seekedDurationInMilles: Int) {
        Log.d(TAG, "onSeeking: " + seekedDurationInMilles)

    }

    private fun getVideoLinkWithSeeking(s: String, videoId: String, currentDuration: Long) {
        val dommyUrl =
            listOfReadyUrls.get(
                java.util.Random().nextInt(listOfReadyUrls.size - 1)
            )
        val item = YoutubeMp3ConverterData(
            0.0,
            dommyUrl,
            "",
            1,
            "",
            ""
        )
        nextTrackWithSeeking(item.link, currentDuration)
//        mainViewModel.getMp3VideoConvertedUrl(
//            "youtube-mp36.p.rapidapi.com",
//            rapidApiKey,
//            videoId
//        )
//        lifecycleScope.launchWhenStarted {
//            mainViewModel.youtubeVideoToMp3StateFlow.collect { event ->
//                when (event) {
//                    is YoutubeVideoToMp3StateFlow.Failed -> {
//                        Log.d(TAG, "video Failed: " + event.errorMessgae)
//                        if (event.errorMessgae.equals(Constants.mp3ApiTooManyRequestsError) || event.errorMessgae.equals(
//                                "Unauthorized"
//                            )
//                        ) {
//                            Log.d(TAG, "getVideoLink: Changing ApiKey")
//                            val newApiKey = changeApiKey(rapidApiKey)
//                            getVideoLink(newApiKey, videoId)
//                        } else {
//                            Log.d(TAG, "getVideoLink: reCallMethod")
//                            getVideoLink(rapidApiKey, videoId)
//
//                        }
//
//                    }
//                    is YoutubeVideoToMp3StateFlow.Success -> {
//                        Log.d(TAG, "onVideoClick: Video Success " + event.item)
//        next(event.item.link)
//
//
//                    }
//                    is YoutubeVideoToMp3StateFlow.Loading -> {
//                        Log.d(TAG, "onVideoClick: Loading")
//                    }
//
//
//                }
//            }

    }


    fun getVideoLink(rapidApiKey: String, videoId: String, item: Item) {
        next(videoId, item)


    }

    private fun initializeBottomNavigationView() {

        binding.mainActivityBottomNavigation.setOnItemSelectedListener(object :
            NavigationView.OnNavigationItemSelectedListener,
            NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {


                if (currentSettedFragment?.javaClass?.name.equals(SearchResultFragment::class.java.name)) {
                    return false
                }
                if (binding.mainActiviytSlidingLayout.isPanelExpanded) {
                    return false
                }
                Log.d(TAG, "onNavigationItemSelected: " + item.itemId)
                when (item.itemId) {

                    R.id.homeFragment -> {
                        if (!isFromPlayListPreview) {
                            setFragmentF(homeFragment)
                            Log.d(TAG, "onNavigationItemSelected: isFromPlayListPreview")
                        } else {
                            setFragment(HomeFragment.playListPreviewFragmen, "", 1)
                            Log.d(TAG, "onNavigationItemSelected: not isFromPlayListPreview")
                        }
                    }
                    R.id.collectionsFragment -> {
                        if (IS_FROM_COLLECTIONFRAGMENT) {
                            setFragment(OUTSIDE_FRAGMENT, "", 1)
                        } else {
                            setFragment(collectionFragment, "", 1)
                        }
                    }

                    R.id.libraryFragment -> setFragment(
                        libraryFragment, "", 1
                    )

                }
                return true
            }

        })
    }

    private fun setFragment(homeFragment1: Fragment, tag: String, i: Int) {
        val fm = supportFragmentManager
        if (homeFragment1.isAdded()) {
            fm.beginTransaction().hide(activeFragment).show(homeFragment1).commit();
        } else {
            fm.beginTransaction()
                .add(R.id.MainActiviyt_nav_host_fragment, homeFragment1, tag)
                .commit();
        }
        activeFragment = homeFragment1;
    }

    override fun playPauseReciver() {
        playPauseAudio()
    }

    override fun next() {

        extractNextAudioUrl()
    }

    override fun previous() {
        extractPreviousAudioUrl()
    }

    override fun stopPlaying() {
        saveCurrentMusicPlayerDataInSharedPref()
        customeMusicPlayer.stop()
    }

    private fun saveCurrentMusicPlayerDataInSharedPref() {
        musicPlayerPersistence.saveLastPlayedList(currentListOfItems)
        musicPlayerPersistence.saveLastPlayedSong(
            LastPlayedSongData(
                currentItem,
                currentItemPosion
            )
        )
    }

    override fun pausing() {
        pause()
        isPlaying = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: ")
        if (resultCode == RESULT_OK) {
            val searchTitle = data?.getStringExtra(SEARCH_TITLE_KEY)!!
            val nSearchResultFragment = SearchResultFragment()
            currentSettedFragment = nSearchResultFragment
            Log.d(TAG, "onActivityResult: $searchTitle")
            nSearchResultFragment.bundleInstance(searchTitle)
            setFragmentF(nSearchResultFragment)
        }
    }

    var IS_FROM_COLLECTIONFRAGMENT = false
    override fun onPlayedTracksClick(tracks: List<Item>) {
        Log.d(
            TAG, "onPlayedTracksClick: " + tracks.get(0).snippet.title.toString()
        )
        val cleanItemList = getCleanListOfItems(tracks)
        RecentlyPlayedTracksFragment.itemsList = tracks
        val recentlyPlayedTracksFragment = RecentlyPlayedTracksFragment.newInstance(
            ItemTypeConverter().convertStandardListOfItemsString(
                cleanItemList
            )
        )

        setFragmentF(recentlyPlayedTracksFragment)
        this.OUTSIDE_FRAGMENT = recentlyPlayedTracksFragment
        activeFragment = recentlyPlayedTracksFragment
        currentSettedFragment = recentlyPlayedTracksFragment
        IS_FROM_COLLECTIONFRAGMENT = true
    }

    private infix fun Hi(num1: Int): Int {
        return num1 * 10
    }

    private fun getCleanListOfItems(tracks: List<Item>): List<Item> {


        val items: MutableList<Item> = mutableListOf()

        return tracks.filter { i ->
            items.add(
                Item(
                    "",
                    "",
                    "",
                    i.snippet,
                    i.playedDateInMillisSecond
                )
            )
        }

    }


}

