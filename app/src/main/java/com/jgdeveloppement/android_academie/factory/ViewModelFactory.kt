package com.jgdeveloppement.android_academie.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jgdeveloppement.android_academie.repository.AcademyRepository
import com.jgdeveloppement.android_academie.viewmodel.AcademyViewModel

class ViewModelFactory(private val academyRepository: AcademyRepository)
    : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AcademyViewModel::class.java))
            return AcademyViewModel(academyRepository) as T

        throw IllegalArgumentException("Unknown class name")
    }
}