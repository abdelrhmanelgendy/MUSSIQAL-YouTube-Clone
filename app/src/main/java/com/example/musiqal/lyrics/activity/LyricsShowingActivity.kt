package com.example.musiqal.lyrics.activity

import RecentlyFoundLyricsAdapter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musiqal.R
import com.example.musiqal.databinding.ActivityLyricsShowingBinding
import com.example.musiqal.fragments.LibraryFragment.Companion.LIST_OF_LYRICS_PARAMS
import com.example.musiqal.fragments.LibraryFragment.Companion.POSITION_PARAMS
import com.example.musiqal.lyrics.LyricsBottomSheet
import com.example.musiqal.lyrics.LyricsUtil
import com.example.musiqal.lyrics.model.SharedPrefLyricsLocalDataModel
import com.example.musiqal.lyrics.util.OnLyricsFoundListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LyricsShowingActivity : AppCompatActivity(),
    RecentlyFoundLyricsAdapter.OnLyricsAdapterItemClickListener {
    lateinit var binding: ActivityLyricsShowingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLyricsShowingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val lyricses = intent.getStringExtra(LIST_OF_LYRICS_PARAMS)
        val position = intent.getIntExtra(POSITION_PARAMS, -1)
        val type = object : TypeToken<List<SharedPrefLyricsLocalDataModel>>() {}.type
        val listOfLyrices = Gson().fromJson<List<SharedPrefLyricsLocalDataModel>>(lyricses, type)
        setUpRecyclerView(listOfLyrices, position)
    }

    private fun setUpRecyclerView(listOfLyrices: List<SharedPrefLyricsLocalDataModel>, position: Int) {
        val adapter = RecentlyFoundLyricsAdapter(this, this)
        adapter.setList(listOfLyrices)
        binding.recentlyPlayedTracksFragmentRecyclerView.run {
            layoutManager = LinearLayoutManager(this@LyricsShowingActivity)
            setAdapter(adapter)
        }
        if (position != -1) {
            binding.recentlyPlayedTracksFragmentRecyclerView.smoothScrollToPosition(position)
        }
    }

    override fun onLyricsClick(position: Int, localLyricsDataModel: SharedPrefLyricsLocalDataModel) {
        openLyricsView(localLyricsDataModel)
    }

    private fun openLyricsView(localLyricsDataModel: SharedPrefLyricsLocalDataModel) {

        val lyricsBottomSheet =
            LyricsBottomSheet.newInstance(lyricsFilter(localLyricsDataModel.songLyrics), R.id.lyricsShowingActivity_coordinatorLayout)
        lyricsBottomSheet.show(supportFragmentManager, "lyricsBottomSheet")


    }

    private fun lyricsFilter(lyrics: String): String {
        val Lyrics_CopyRight = "Paroles de la chanson"

        return lyrics.replace(Lyrics_CopyRight, "")


    }
}