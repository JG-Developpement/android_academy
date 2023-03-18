package com.jgdeveloppement.android_academie.retrofit

import com.jgdeveloppement.android_academie.models.Article
import com.jgdeveloppement.android_academie.models.InterviewAsk
import com.jgdeveloppement.android_academie.models.Quiz

class ApiHelper(private val apiService: ApiService):ApiService {

    override suspend fun getArticles(key: String): List<Article> = apiService.getArticles(key)

    override suspend fun getArticleById(id: Int, key: String): List<Article> = apiService.getArticleById(id, key)

    override suspend fun getInterviewAsks(key: String): List<InterviewAsk> = apiService.getInterviewAsks(key)

    override suspend fun getQuiz(key: String): List<Quiz> = apiService.getQuiz(key)


}