package com.example.musiqal.fragments

import LyricsOfAllTracksAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.musiqal.R
import com.example.musiqal.databinding.FragmentLibraryBinding
import com.example.musiqal.fragments.library.adapter.OnLyricsTrackClickListener
import com.example.musiqal.lyrics.activity.LyricsShowingActivity
import com.example.musiqal.lyrics.lyricsdatabase.LyricsSharedPreferences
import com.example.musiqal.lyrics.lyricsdatabase.local.LyricsDatabase
import com.example.musiqal.lyrics.lyricsdatabase.local.mvi.LyricsDatabaseViewModel
import com.example.musiqal.lyrics.model.LyricsLocalDataModel
import com.example.musiqal.lyrics.model.SharedPrefLyricsLocalDataModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.collect
import java.util.ArrayList

class LibraryFragment : Fragment(R.layout.fragment_library), OnLyricsTrackClickListener {
    companion object {
        val LIST_OF_LYRICS_PARAMS = "list_of_lyrics"
        val POSITION_PARAMS = "position"
    }

    lateinit var lyricsSharedPreferences:LyricsSharedPreferences
    private lateinit var allLyricsTracks: List<SharedPrefLyricsLocalDataModel>
    private val TAG = "LibraryFragmentTAG"
    val lyricsDatabaseViewModel: LyricsDatabaseViewModel by lazy {
        ViewModelProvider(requireActivity()).get(lyricsDatabaseViewModel::class.java)
    }
    lateinit var binding: FragmentLibraryBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
        binding.libraryFragmentBtnOpenLyricesActivity.setOnClickListener {
            openAllLyricsActivity(this@LibraryFragment.allLyricsTracks, -1)
        }
        binding.collectionFragmentLayoutBtnOpenHistory.setOnClickListener {
            openAllLyricsActivity(this@LibraryFragment.allLyricsTracks, -1)

        }
        binding.collectionFragmentSwipeRefresher.setOnRefreshListener {
            getData()
        }
    }

    private fun getData() {
        lifecycleScope.launchWhenStarted {
//            val localLyricsDao =
//                Room.databaseBuilder(requireContext(), LyricsDatabase::class.java, "database")
//                    .build()
//                    .getLocalLyricsDao()
            lyricsSharedPreferences = LyricsSharedPreferences(requireContext())

            val allTracksLyrics = lyricsSharedPreferences.getSaved()
            setUpdata(allTracksLyrics)
            this@LibraryFragment.allLyricsTracks = allTracksLyrics
            binding.collectionFragmentSwipeRefresher.isRefreshing = false
        }

    }

    private fun setUpdata(allTracksLyrics: List<SharedPrefLyricsLocalDataModel>) {
        val adapter = LyricsOfAllTracksAdapter(requireContext(), this)
        adapter.setList(allTracksLyrics)

        binding.libraryFragmentRecyclerViewAllLyrics.run {
            layoutManager =
                (LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false))
            setAdapter(adapter)
        }
    }

    override fun onTrackClick(
        lyricsLocalDataModel: SharedPrefLyricsLocalDataModel,
        position: Int,
        playedTracks: ArrayList<SharedPrefLyricsLocalDataModel>
    ) {
        Log.d(TAG, "onTrackClick: " + playedTracks)
        openAllLyricsActivity(playedTracks, position)
    }

    private fun openAllLyricsActivity(
        playedTracks: List<SharedPrefLyricsLocalDataModel>,
        position: Int
    ) {
        val lyricsShowingActivityIntent =
            Intent(requireActivity(), LyricsShowingActivity::class.java)
        lyricsShowingActivityIntent.putExtra(LIST_OF_LYRICS_PARAMS, Gson().toJson(playedTracks))
        lyricsShowingActivityIntent.putExtra(POSITION_PARAMS, position)
        startActivity(lyricsShowingActivityIntent)
    }

}