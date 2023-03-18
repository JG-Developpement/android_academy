package com.jgdeveloppement.android_academie.viewmodel

import androidx.lifecycle.ViewModel
import com.jgdeveloppement.android_academie.models.Bookmark
import com.jgdeveloppement.android_academie.repository.AcademyRepository

class AcademyViewModel(private val academyRepository: AcademyRepository): ViewModel() {

    //Room
    fun getBookmarkByArticleId(articleId: Int) = academyRepository.getBookmarkByArticleId(articleId)
    fun insertBookmark(bookmark: Bookmark) = academyRepository.insertBookmark(bookmark)
    fun deleteBookmark(articleId: Int) = academyRepository.deleteBookmark(articleId)
    //Retrofit
    fun getArticles() = academyRepository.getArticles()
    fun getInterviewAsks() = academyRepository.getInterviewAsks()
    fun getQuiz() = academyRepository.getQuiz()
    //Other
    fun getArticleBookmarked() = academyRepository.getArticleBookmarked()
}