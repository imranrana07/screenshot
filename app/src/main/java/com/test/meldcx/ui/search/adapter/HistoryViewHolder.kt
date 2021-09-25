package com.test.meldcx.ui.search.adapter

import androidx.recyclerview.widget.RecyclerView
import test.meldcx.databinding.ItemHistoriesBinding

class HistoryViewHolder(itemView: ItemHistoriesBinding): RecyclerView.ViewHolder(itemView.root) {
    val ivSS = itemView.ivSS
    val tvUrl = itemView.tvUrl
    val tvDate = itemView.tvDate
    val btnDelete = itemView.btnDelete
}
