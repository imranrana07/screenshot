package com.test.meldcx.data.api

import com.google.gson.annotations.SerializedName

data class BaseResponse<T> (
        @SerializedName("current_page")
        val currentPage: Int,
        val data: ArrayList<T>,
        @SerializedName("last_page")
        val lastPage: Int,
)