package com.jgdeveloppement.android_academie.injection

import android.content.Context
import com.jgdeveloppement.android_academie.factory.ViewModelFactory
import com.jgdeveloppement.android_academie.repository.AcademyRepository
import com.jgdeveloppement.android_academie.retrofit.ApiHelper
import com.jgdeveloppement.android_academie.retrofit.RetrofitBuilder
import com.jgdeveloppement.android_academie.room.AcademyDatabase
import com.jgdeveloppement.android_academie.room.AcademyHelper

object Injection {

    private fun provideAcademyRepository(context: Context): AcademyRepository{
        val database = AcademyDatabase.getDatabase(context)
        val bookmarkDao = database.bookmarkDao()
        val academyHelper = AcademyHelper(bookmarkDao)
        val apiHelper = ApiHelper(RetrofitBuilder.apiService)
        return AcademyRepository(academyHelper, apiHelper)
    }

    fun provideAcademyViewModelFactory(context: Context): ViewModelFactory{
        val academyRepository = provideAcademyRepository(context)
        return ViewModelFactory(academyRepository)
    }
}