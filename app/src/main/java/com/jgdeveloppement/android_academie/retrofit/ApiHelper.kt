package com.jgdeveloppement.android_academie.retrofit

import com.jgdeveloppement.android_academie.models.Article
import com.jgdeveloppement.android_academie.models.InterviewAsk
import com.jgdeveloppement.android_academie.models.Quiz

class ApiHelper(private val apiService: ApiService):ApiService {

    override suspend fun getArticles(key: String): List<Article> = apiService.getArticles(key)
    override suspend fun getEnArticles(key: String): List<Article> = apiService.getEnArticles(key)

    override suspend fun getArticleById(id: Int, key: String): List<Article> = apiService.getArticleById(id, key)
    override suspend fun getEnArticleById(id: Int, key: String): List<Article> = apiService.getEnArticleById(id, key)

    override suspend fun getInterviewAsks(key: String): List<InterviewAsk> = apiService.getInterviewAsks(key)
    override suspend fun getEnInterviewAsks(key: String): List<InterviewAsk> = apiService.getEnInterviewAsks(key)

    override suspend fun getQuiz(key: String): List<Quiz> = apiService.getQuiz(key)
    override suspend fun getEnQuiz(key: String): List<Quiz> = apiService.getEnQuiz(key)


}