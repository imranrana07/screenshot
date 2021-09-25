package com.test.meldcx.ui.webview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.meldcx.data.local.database.dao.HistoryDao
import com.test.meldcx.data.model.entities.HistoryEntity
import com.test.meldcx.data.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(private val searchHistoryRepository: HistoryRepository) :ViewModel() {

    fun addHistory(history: HistoryEntity){
        viewModelScope.launch(Dispatchers.IO) {
            searchHistoryRepository.addHistory(history)
        }
    }
}