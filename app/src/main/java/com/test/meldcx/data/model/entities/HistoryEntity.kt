package com.test.meldcx.data.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable

@Entity(tableName = "histories")
data class HistoryEntity(
        val imageUrl: String,
        val dateTime: String,
        val webUrl: String?
) : Serializable{
        @PrimaryKey(autoGenerate = true)
        var id:Int = 0
}