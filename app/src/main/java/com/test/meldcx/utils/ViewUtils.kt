package com.test.meldcx.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(message:String){
    Toast.makeText(context,message,Toast.LENGTH_LONG).show()
}
fun Context.toast(message:String){
    Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
}