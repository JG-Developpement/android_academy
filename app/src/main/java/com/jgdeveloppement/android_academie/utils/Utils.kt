package com.jgdeveloppement.android_academie.utils

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.jgdeveloppement.android_academie.models.Article

object Utils {
    lateinit var myApp: AppCompatActivity
    var articleListSearch: MutableList<Article> = mutableListOf()

    fun showSnackBar(view: View?, message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
    }



    fun reactToError(progressLayout: View, message: String, layout: View, logMessage: String){
        progressLayout.visibility = View.GONE
        showSnackBar(layout, message)
        Log.e("DEBUGGG", logMessage)
    }
}