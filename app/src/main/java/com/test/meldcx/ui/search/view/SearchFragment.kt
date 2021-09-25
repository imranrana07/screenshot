package com.test.meldcx.ui.search.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.meldcx.core.ClickListener
import com.test.meldcx.core.DataPass
import com.test.meldcx.data.model.entities.HistoryEntity
import com.test.meldcx.ui.search.adapter.HistoryAdapter
import com.test.meldcx.ui.search.viewmodel.HistorySearchViewModel
import com.test.meldcx.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import test.meldcx.R
import test.meldcx.databinding.FragmentHistorySearchBinding

@AndroidEntryPoint
class SearchFragment : Fragment(),ClickListener<DataPass<HistoryEntity>>,SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentHistorySearchBinding
    private lateinit var viewModelHistory: HistorySearchViewModel

    private val adapter: HistoryAdapter by lazy { HistoryAdapter(this)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistorySearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelHistory = ViewModelProvider(this).get(HistorySearchViewModel::class.java)

        // just to feel the shimmer effect
        showShimmer()
        binding.searchView.setOnQueryTextListener(this)
    }

    private fun initData(){
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistories.layoutManager = layoutManager
        binding.rvHistories.adapter = adapter
        emptyMessage()

        lifecycleScope.launch(Dispatchers.IO) {
            viewModelHistory.getHistories().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun showShimmer(){
        lifecycleScope.launch {
            binding.includeShimmer.sLayout.visibility = VISIBLE
            binding.includeShimmer.sLayout.startShimmerAnimation()
            delay(2000)
            binding.includeShimmer.sLayout.visibility = GONE
            binding.includeShimmer.sLayout.stopShimmerAnimation()
            initData()
        }
    }

    override fun clickedData(data: DataPass<HistoryEntity>) {
        if (data.selection == DELETE){
            viewModelHistory.deleteHistory(data.data)
            toast("Deleted Successfully")
        }else{
            val bundle = bundleOf(WEB_URL to data.data.webUrl)
            findNavController().navigate(R.id.webViewFragment, bundle)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query!!.isNotEmpty()) {
            searchHistories(query)
        }else{
            initData()
        }
        return true
    }

    private fun searchHistories(searchQuery:String){
        val query = "%$searchQuery%"
        lifecycleScope.launch {
            viewModelHistory.searchHistories(query).collectLatest {
                emptyMessage()
                adapter.submitData(viewLifecycleOwner.lifecycle,it)
            }
        }
    }

    private fun emptyMessage(){
        adapter.addLoadStateListener {
            if (adapter.itemCount == 0){
                binding.tvEmptyView.visibility = VISIBLE
            }else{
                binding.tvEmptyView.visibility = GONE
            }
        }
    }
}