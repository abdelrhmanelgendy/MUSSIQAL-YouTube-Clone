package com.example.musiqal.search

import YoutubeSearchAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musiqal.databinding.ActivitySearchBinding
import com.example.musiqal.dialogs.SimpleYesOrNoDialog
import com.example.musiqal.dialogs.util.OnDialogButtonsClickListener
import com.example.musiqal.fragments.SearchResultFragment
import com.example.musiqal.search.database.SearchHistoryData
import com.example.musiqal.search.mvi.SearchViewModel
import com.example.musiqal.search.mvi.SearchViewState
import com.example.musiqal.ui.MainActivity
import com.example.musiqal.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), OnHistoryDataClickListener,
    OnDialogButtonsClickListener {

    companion object {
        val SEARCH_TITLE_KEY = "search_title"
    }

    val dialogSubText = "Remove from history?"
    val dialogPositiveText = "REMOVE"
    val dialogNegativeText = "CANCEL"

    lateinit var searchViewModel: SearchViewModel
    lateinit var youtubeSearchAdapter: YoutubeSearchAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var binding: ActivitySearchBinding
    lateinit var simpleYesOrNoDialog: SimpleYesOrNoDialog
    lateinit var savedLististoryData: List<SearchHistoryData>
//    lateinit var onHistoryTitleClickListener: OnHistoryTitleClickListener

    private val TAG = "SearchActivity11"
    var fromRetrivingDataFromDataBase = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        setContentView(binding.root)

        simpleYesOrNoDialog = SimpleYesOrNoDialog(context = this)

        intiRecyclerViews()
        attachSearchActionToEtSearch()
        setUpETSearchTextChange()


        intent?.let {
            val lastSearchQuery = it.getStringExtra(SearchResultFragment.LAST_SEARCH_QYERY_KEY)
            binding.serchActivityETSearch.setText(lastSearchQuery)
        }

        binding.serchActivityImgBack.setOnClickListener {
            onBackPressed()
        }
        binding.serchActivityImgDeleteEditText.setOnClickListener {
            binding.serchActivityETSearch.setText("")
            retreiveSavedData()
        }
    }

    private fun setUpETSearchTextChange() {
        Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(e: ObservableEmitter<String>) {
                binding.serchActivityETSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        Log.d(TAG, "onTextChanged: " + s.toString())
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (s.toString().equals("")) {

                            Log.d(TAG, "onTextChanged2: empty")
                            retreiveSavedData()

                        } else {
                            fromRetrivingDataFromDataBase = false
                            startSearching(s.toString())
                            Log.d(TAG, "onTextChanged2: not empty")
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }
                })
            }
        }).subscribeOn(Schedulers.computation())
            .subscribe { searchTxt -> startSearching(searchTxt) }


    }

    private fun startSearching(searchTxt: String) {

        searchViewModel.getAutoCompleteQueries(searchTxt)
        setUpViewModelStateHistoryStateFlow()


    }


    private fun attachSearchActionToEtSearch() {
        binding.serchActivityETSearch.setOnEditorActionListener(object :
            TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val searchText = v?.editableText.toString()
                    val searchTimInMillis = System.currentTimeMillis().toString()
                    if (searchText.length > 0) {
                        searchYoutubeQuery(searchText)
                    }
                    return true
                }
                return false
            }
        }
        )
    }

    private fun saveSearchData(searchText: String, searchTime: String) {

        val hisSearchHistoryData = SearchHistoryData(searchText, searchTime)
        searchViewModel.insertHistoryData(hisSearchHistoryData)

    }

    private fun retreiveSavedData() {
        Log.d(TAG, "retreiveSavedData: 123")
        searchViewModel.getAllSearchData()
        setUpViewModelStateHistoryStateFlow()
    }


    private fun intiRecyclerViews() {
        youtubeSearchAdapter = YoutubeSearchAdapter(this, this)
        linearLayoutManager = LinearLayoutManager(this)
        binding.serchActivityRecyclerView.layoutManager = linearLayoutManager
        binding.serchActivityRecyclerView.adapter = youtubeSearchAdapter
        setUpRecyclerViewLeftRightSwipeToDelete()
    }


    private fun setUpDataIntoRecyclerView(dataSearchHistoryData: List<SearchHistoryData>) {
        youtubeSearchAdapter.setList(dataSearchHistoryData)
    }

    override fun onDataSearchClick(searchHistoryData: SearchHistoryData, position: Int) {
        Log.d(TAG, "onActivityResult 2: " + searchHistoryData.searchTitle)
        searchYoutubeQuery(searchHistoryData.searchTitle)


    }

    private fun searchYoutubeQuery(searchQuery: String) {
        saveSearchData(searchQuery, System.currentTimeMillis().toString())

        val searchedIntent = Intent(applicationContext, MainActivity::class.java)
        searchedIntent.putExtra(SEARCH_TITLE_KEY, searchQuery)
        setResult(RESULT_OK, searchedIntent)
        finish()

    }

    override fun onTopArrowClick(searchHistoryData: SearchHistoryData, position: Int) {
        Log.d(TAG, "onTopArrowClick: " + searchHistoryData)
        binding.serchActivityETSearch.setText(searchHistoryData.searchTitle)

    }

    override fun onDataSearchLongClick(
        listOfSearchData: List<SearchHistoryData>,
        searchHistoryData: SearchHistoryData,
        position: Int
    ) {


        deleteItemDialog(position)
    }

    fun deleteFromSearchHistory(searchHistoryData: SearchHistoryData) {
        searchViewModel.deleteSearchHistory(searchHistoryData)
        simpleYesOrNoDialog.dismis()


        CoroutineScope(Dispatchers.Main).launch {
            retreiveSavedData()
        }

    }

    override fun onPositiveButtonClick(clickedPosition: Int) {
        val searchData = this.savedLististoryData.get(clickedPosition)
        deleteFromSearchHistory(searchData)

    }

    override fun onNegativeButtonClick(clickedPosition: Int) {
        simpleYesOrNoDialog.dismis()
    }

    fun setUpRecyclerViewLeftRightSwipeToDelete() {
        val touchListener = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapterPosition = viewHolder.adapterPosition
                deleteItemDialog(adapterPosition)
                youtubeSearchAdapter.notifyItemChanged(adapterPosition)
            }

        }
        val itemTouchHelper = ItemTouchHelper(touchListener)
        itemTouchHelper.attachToRecyclerView(binding.serchActivityRecyclerView)
    }

    fun deleteItemDialog(position: Int) {
        if (!youtubeSearchAdapter.listOfHistoryData.get(position).searchHistory.equals("-1")) {
            val searchHistoryData = savedLististoryData.get(position)
            simpleYesOrNoDialog.intialize(
                searchHistoryData.searchTitle,
                dialogSubText,
                dialogPositiveText,
                dialogNegativeText,
                position,
                true
            )
            simpleYesOrNoDialog.show(false)
        }
    }

    private fun setUpViewModelStateHistoryStateFlow() {
        lifecycleScope.launch {
            searchViewModel.searchHistoryStateFlow.collect { value ->
                when (value) {
                    is SearchViewState.Idel -> {
                        Log.d(TAG, "retreiveSavedData: IDLE ")
                    }
                    is SearchViewState.Loading -> {
                        Log.d(TAG, "retreiveSavedData: Loading ")
                    }
                    is SearchViewState.Success -> {

                        if (fromRetrivingDataFromDataBase) {
                            val reversedListOfHistory = value.listOfSearchData.reversed()
                            setUpDataIntoRecyclerView(reversedListOfHistory)
                            savedLististoryData = reversedListOfHistory
                        } else {
                            val reversedListOfHistory = value.listOfSearchData
                            setUpDataIntoRecyclerView(reversedListOfHistory)
                        }
                        Log.d(TAG, "retreiveSavedData: " + value.listOfSearchData.toString())
                    }
                    is SearchViewState.Failed -> {
                        Log.d(TAG, "failedRetreiveSavedData: " + value)
                        throw Exception(value.errorMessgae)
                    }
                }
            }

        }
    }

    fun getRandomApiKey(): String {
        return Constants.getRandomYoutubeDataKey(this)

    }

    override fun onBackPressed() {
        finish()
    }
}