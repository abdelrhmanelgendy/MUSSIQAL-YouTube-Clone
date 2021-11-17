package com.example.musiqal.fragments

import RecentlyPlayedTracksAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musiqal.databinding.FragmentRecentlyPlayedTracksBinding
import com.example.musiqal.helpers.CalenderHelper
import com.example.musiqal.datamodels.youtubeItemInList.Item
import com.example.musiqal.recyclerViewAdapters.collectionsAdapter.util.OnTrackClickListener
import com.example.musiqal.util.OnAudioInPlaylistClickListner
import java.lang.Exception
import java.util.*

class RecentlyPlayedTracksFragment : Fragment(), OnTrackClickListener {
    private val TAG = "RecentlyPlayedTracks11"
    lateinit var binding: FragmentRecentlyPlayedTracksBinding
    lateinit var calenderHelper: CalenderHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calenderHelper = CalenderHelper()
    }

    lateinit var onAudioInPlaylistClickListner: OnAudioInPlaylistClickListner
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnAudioInPlaylistClickListner) {
            onAudioInPlaylistClickListner = context
        } else {
            throw Exception("you must implement On Item Listener")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecentlyPlayedTracksBinding.inflate(layoutInflater)
        setProgressBarVisibility(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = LinearLayoutManager(requireContext())
        setUpDataIntoRecyclerView(RecentlyPlayedTracksFragment.itemsList)
        binding.recentlyPlayedTracksFragmentImgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        setUpRecyclerViewScrollListener()


    }

    private fun setUpRecyclerViewScrollListener() {

        binding.recentlyPlayedTracksFragmentScrollView.viewTreeObserver.addOnScrollChangedListener {

            changeDateTextView()

        }
    }

    private fun changeDateTextView() {
        val firstVisiblePosition =
            (binding.recentlyPlayedTracksFragmentRecyclerView.layoutManager as LinearLayoutManager)
                .findFirstCompletelyVisibleItemPosition()
        val itemsList = RecentlyPlayedTracksFragment.itemsList.reversed()
        val playedDateInMillisSecond = itemsList.get(firstVisiblePosition).playedDateInMillisSecond
        calenderHelper.setCalender(playedDateInMillisSecond.toLong())
        val day = calenderHelper.getDay()
        val month = calenderHelper.getMonth()+1
        val year = calenderHelper.getYear()

        val monthName = calenderHelper.getMonthName()
        if (isPLayedDateIsToday(day, month, year)) {
            binding.recentlyPlayedTracksFragmentTVItemsDate.setText("Today")

        } else {
            binding.recentlyPlayedTracksFragmentTVItemsDate.setText("$day $monthName")
        }


    }


    private fun isPLayedDateIsToday(day: Int, month: Int, year: Int): Boolean {
        val currentCalender = Calendar.getInstance()
        val currentDay = currentCalender.get(Calendar.DAY_OF_MONTH)
        val currentYear = currentCalender.get(Calendar.YEAR)
        val currentMonth = currentCalender.get(Calendar.MONTH)+1
        Log.d(TAG, "isPLayedDateIsToday: $currentDay $day")
        Log.d(TAG, "isPLayedDateIsToday: $currentYear $year")
        Log.d(TAG, "isPLayedDateIsToday: $currentMonth $month")
        return (day == currentDay && currentYear == year && currentMonth == month)
    }

    private fun getCurrentItem(): Int {
        return (binding.recentlyPlayedTracksFragmentRecyclerView.getLayoutManager() as LinearLayoutManager)
            .findFirstVisibleItemPosition()
    }

    lateinit var layoutManager: LinearLayoutManager

    fun setUpDataIntoRecyclerView(list: List<Item>) {
        Log.d(TAG, "setUpDataIntoRecyclerView: setting")

        val historyOfPlayedTracksAdapter = RecentlyPlayedTracksAdapter(requireContext(), this)
        binding.recentlyPlayedTracksFragmentRecyclerView.also {
            it.adapter = historyOfPlayedTracksAdapter
            it.layoutManager = layoutManager
        }
        historyOfPlayedTracksAdapter.setList(list)
        setProgressBarVisibility(false)

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
            "Recently Played"
        )

    }

    companion object {
        var itemsList: List<Item> = listOf()
        val PARAM_1_KEY = "paramKey"

        @JvmStatic
        fun newInstance(param1: String) =
            RecentlyPlayedTracksFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_1_KEY, param1)
                }
            }
    }
    fun setProgressBarVisibility(isProgressBarVisble:Boolean)
    {
        if (isProgressBarVisble)
        {
            binding.recentlyPlayedTracksFragmentProgressabar.visibility=View.VISIBLE
            binding.recentlyPlayedTracksFragmentLinearLayoutAllViews.visibility=View.GONE
        }
        else
        {
            binding.recentlyPlayedTracksFragmentProgressabar.visibility=View.GONE
            binding.recentlyPlayedTracksFragmentLinearLayoutAllViews.visibility=View.VISIBLE
        }

    }
}