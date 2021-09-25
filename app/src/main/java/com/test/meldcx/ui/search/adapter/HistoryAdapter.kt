package com.test.meldcx.ui.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.test.meldcx.core.ClickListener
import com.test.meldcx.core.DataPass
import com.test.meldcx.data.model.entities.HistoryEntity
import com.test.meldcx.utils.DELETE
import test.meldcx.R
import test.meldcx.databinding.ItemHistoriesBinding

class HistoryAdapter(private val clickListener: ClickListener<DataPass<HistoryEntity>>):
    PagingDataAdapter<HistoryEntity,HistoryViewHolder>(HistoryDiff) {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        context = parent.context
        val view = ItemHistoriesBinding.inflate(LayoutInflater.from(context),parent,false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = getItem(position)!!
//        val history = list[position]
        Glide.with(context).load(history.imageUrl).placeholder(R.drawable.error).into(holder.ivSS)
        holder.tvDate.text = history.dateTime
        holder.tvUrl.text = history.webUrl

        holder.ivSS.setOnClickListener {
            clickListener.clickedData(DataPass(history,null))
        }

        holder.tvUrl.setOnClickListener {
            clickListener.clickedData(DataPass(history,null))
        }

        holder.btnDelete.setOnClickListener {
            clickListener.clickedData(DataPass(history,DELETE))
        }
    }

    object HistoryDiff : DiffUtil.ItemCallback<HistoryEntity>(){
        override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem == newItem
        }
    }
}