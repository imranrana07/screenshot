package com.test.meldcx.ui.webview

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.test.meldcx.data.model.entities.HistoryEntity
import com.test.meldcx.utils.WEB_URL
import com.test.meldcx.utils.hasPermissions
import com.test.meldcx.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import test.meldcx.R
import test.meldcx.databinding.FragmentWebViewBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

private val storagePermission = arrayOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

@AndroidEntryPoint
class WebViewFragment : Fragment() {
    private lateinit var binding: FragmentWebViewBinding
    private lateinit var viewModel: WebViewViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        binding = FragmentWebViewBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickEvents()

        val webUrl = requireArguments().getString(WEB_URL)
        initializedWebView(webUrl!!)

        viewModel = ViewModelProvider(this).get(WebViewViewModel::class.java)
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun initializedWebView(webUrl:String){
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.allowContentAccess = true
        binding.webView.settings.loadWithOverviewMode = true

        webViewClient()
        if (webUrl != null) {
            binding.webView.loadUrl(webUrl)
        }
    }

    private fun clickEvents(){
        binding.btnCapture.setOnClickListener {
            if (hasPermissions(requireContext(), storagePermission)) {
                capture()
            }else{
                permissionReqLauncher.launch(storagePermission,)
            }
        }


        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnHistory.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
    }

    private fun capture(){
        val bitmap = getScreenShotFromView(binding.webView)
        if (bitmap != null) {
            saveMediaToStorage(bitmap)
        }
    }

    private fun webViewClient(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.webView.webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.progressBarWevView.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.progressBarWevView.visibility = View.GONE
                }

                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    if (request?.url?.path?.endsWith(".pdf")!!) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(request.url.toString())))
                    }
                    return false
                }
            }
        }
    }

    //take screen shot
    private fun getScreenShotFromView(v: View): Bitmap? {
        // create a bitmap object
        var screenshot: Bitmap? = null
        try {
            screenshot = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {
            Log.v("GFG", "Failed to capture screenshot because:" + e.message)
        }
        // return the bitmap
        return screenshot
    }

    // this method saves the image to gallery
    private fun saveMediaToStorage(bitmap: Bitmap) {
        // Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        // Output stream
        var fos: OutputStream? = null

        // For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // getting the contentResolver
            requireActivity().contentResolver?.also { resolver ->

                // Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    // putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                // Inserting the contentValues to
                // contentResolver and getting the Uri
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                saveImageToDB(imageUri.toString())
                // Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            // These for devices running on android < Q
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
            saveImageToDB(image.toString())
        }

        fos?.use {
            // Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }

    private fun saveImageToDB(imageUrl:String){
        val history = HistoryEntity(
            imageUrl,
            time(),
            binding.webView.url
        )

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.addHistory(history)
        }

        toast("Screenshot taken and saved")
    }

    private fun time(): String {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy.MM.dd") //or use getDateInstance()
        return formatter.format(date)
    }

    private val permissionReqLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permission -> run{
        val granted = permission.entries.all {
            it.value == true
        }
        if (granted){
            capture()
        }
            }
    }
}