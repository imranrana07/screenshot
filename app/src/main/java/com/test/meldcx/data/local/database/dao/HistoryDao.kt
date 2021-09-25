package com.test.meldcx.data.local.database.dao;

import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.test.meldcx.data.model.entities.HistoryEntity

@Dao
interface HistoryDao {

    @Insert(onConflict = REPLACE)
    suspend fun add(entity: HistoryEntity)

    @Query("SELECT * FROM histories ORDER BY id DESC")
    fun getHistories(): PagingSource<Int,HistoryEntity>

    @Query("SELECT * FROM histories WHERE webUrl LIKE :searchQuery ORDER BY id DESC")
    fun searchHistories(searchQuery: String): PagingSource<Int,HistoryEntity>

    @Delete
    suspend fun deleteHistory(entity: HistoryEntity)
}
