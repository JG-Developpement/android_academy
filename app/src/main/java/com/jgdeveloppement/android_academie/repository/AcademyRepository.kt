package com.jgdeveloppement.android_academie.repository

import androidx.lifecycle.liveData
import com.jgdeveloppement.android_academie.BuildConfig
import com.jgdeveloppement.android_academie.models.Article
import com.jgdeveloppement.android_academie.models.Bookmark
import com.jgdeveloppement.android_academie.retrofit.ApiHelper
import com.jgdeveloppement.android_academie.room.AcademyHelper
import com.jgdeveloppement.android_academie.utils.Resource
import kotlinx.coroutines.Dispatchers

class AcademyRepository(private val academyHelper: AcademyHelper, private val apiHelper: ApiHelper) {

    //////////
    //Room
    /////////

    private val key = BuildConfig.API_KEY

    fun getBookmarkByArticleId(articleId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = academyHelper.getBookmarkByArticleId(articleId)))
        }catch (exception: Exception){
            emit(Resource.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun insertBookmark(bookmark: Bookmark) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            var bookmarkDao = academyHelper.getBookmarkByArticleId(bookmark.articleId)
            if (bookmarkDao == null)
                bookmarkDao = bookmark
            else
                bookmarkDao.page = bookmark.page
            emit(Resource.success(data = academyHelper.insertBookmark(bookmarkDao)))
        }catch (exception: Exception){
            emit(Resource.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun deleteBookmark(articleId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = academyHelper.deleteBookmark(articleId)))
        }catch (exception: Exception){
            emit(Resource.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    ////////////
    //Retrofit
    ////////////

    fun getArticles() = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiHelper.getArticles(key)))
        }catch (exception: Exception){
            emit(Resource.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getInterviewAsks() = liveData(Dispatchers.IO){
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiHelper.getInterviewAsks(key)))
        }catch (exception: Exception){
            emit(Resource.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getQuiz() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiHelper.getQuiz(key)))
        }catch (exception: Exception){
            emit(Resource.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    //////////
    //Other
    /////////

    fun getArticleBookmarked() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val bookmarks = academyHelper.getAllBookmark()
            val articlesBookmarked: MutableList<Article> = mutableListOf()
            bookmarks.forEach {
                val article = apiHelper.getArticleById(it.articleId, key)
                articlesBookmarked.add(article[0])
            }
            emit(Resource.success(data = articlesBookmarked))
        }catch (exception: Exception){
            emit(Resource.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }
}