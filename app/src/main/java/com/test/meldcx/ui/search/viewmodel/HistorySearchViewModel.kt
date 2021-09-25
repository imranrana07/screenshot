package com.test.meldcx.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.test.meldcx.data.local.database.dao.HistoryDao
import com.test.meldcx.data.model.entities.HistoryEntity
import com.test.meldcx.data.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistorySearchViewModel @Inject constructor(private val repository: HistoryRepository/*historyDao: HistoryDao*/) :ViewModel() {
//    private val repository = HistoryRepository(historyDao)

    fun getHistories(): Flow<PagingData<HistoryEntity>>{
            return Pager(
                config = PagingConfig(
                    pageSize = 10
                ),
                pagingSourceFactory = {repository.getHistories()}
            ).flow.cachedIn(viewModelScope)
    }

    fun searchHistories(searchQuery:String): Flow<PagingData<HistoryEntity>>{
            return Pager(
                config = PagingConfig(
                    pageSize = 10
                ),
                pagingSourceFactory = {repository.searchHistories(searchQuery)}
            ).flow.cachedIn(viewModelScope)
    }

    fun deleteHistory(history: HistoryEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteHistory(history)
        }
    }

}