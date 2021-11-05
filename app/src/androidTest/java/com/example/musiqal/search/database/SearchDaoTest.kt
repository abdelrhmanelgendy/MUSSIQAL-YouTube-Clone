package com.example.musiqal.search.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.musiqal.database.local.HistoryOfSearchingDao
import com.example.musiqal.database.local.PlaylistsDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
//@SmallTest
class SearchDaoTest {

    @get:Rule
    val instantTask= InstantTaskExecutorRule()

    private lateinit var searchDao: HistoryOfSearchingDao
    private lateinit var dataBase: PlaylistsDatabase
    val searchHistoryData=SearchHistoryData("search Title","11-2-2020")

    @Before
    fun setUp() {
        dataBase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PlaylistsDatabase::class.java
        ).build()
        searchDao=dataBase.getHistoryOfSearchingDao()
    }

    @After
    fun tearDown(){
        dataBase.close()
    }

    @Test
    fun testInsertIntoSearch()
    {
        runBlockingTest {
            searchDao.insertHistory(searchHistoryData)
            val listOfSavedData =searchDao.getAllSearchData()
            assertThat(listOfSavedData).contains(searchHistoryData)
        }


    }

    @Test
    fun testDeleteSingleItemFromSearch()
    {
        var listOfSavedData: List<SearchHistoryData>? = null
        runBlockingTest {

            searchDao.insertHistory(searchHistoryData)
            searchDao.deleteAllHistory()
            listOfSavedData = searchDao.getAllSearchData()
        }

        assertThat(listOfSavedData).doesNotContain(searchHistoryData)

    }

}