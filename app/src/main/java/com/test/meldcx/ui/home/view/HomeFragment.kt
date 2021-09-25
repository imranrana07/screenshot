package com.test.meldcx.ui.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.test.meldcx.utils.WEB_URL
import test.meldcx.R
import test.meldcx.databinding.FragmentHomeBinding

//@AndroidEntryPoint
class HomeFragment : Fragment(){
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickEvents()
    }

    private fun clickEvents(){

        binding.btnGo.setOnClickListener {
            if (isUrlValid()) {
                val bundle = bundleOf(WEB_URL to binding.etUrl.text.toString())
                findNavController().navigate(R.id.webViewFragment, bundle)
            }else{
                binding.etUrl.error = "Invalid Url"
            }
        }

        binding.btnHistory.setOnClickListener { findNavController().navigate(R.id.searchFragment) }
    }

    private fun isUrlValid(): Boolean{
        return URLUtil.isValidUrl(binding.etUrl.text.toString())
    }

}