package com.test.meldcx.data.repository

import androidx.paging.PagingSource
import com.test.meldcx.data.local.database.dao.HistoryDao
import com.test.meldcx.data.model.entities.HistoryEntity
import javax.inject.Inject

class HistoryRepository @Inject constructor(private val historyDao: HistoryDao) {

    suspend fun addHistory(history: HistoryEntity){
        return historyDao.add(history)
    }

    fun getHistories(): PagingSource<Int,HistoryEntity>{
        return historyDao.getHistories()
    }

    fun searchHistories(searchQuery: String): PagingSource<Int,HistoryEntity>{
        return historyDao.searchHistories(searchQuery)
    }

    suspend fun deleteHistory(history: HistoryEntity){
        return historyDao.deleteHistory(history)
    }

}