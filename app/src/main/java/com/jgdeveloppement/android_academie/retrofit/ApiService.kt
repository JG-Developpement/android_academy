package com.jgdeveloppement.android_academie.retrofit

import com.jgdeveloppement.android_academie.models.Article
import com.jgdeveloppement.android_academie.models.InterviewAsk
import com.jgdeveloppement.android_academie.models.Quiz
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("27/{key}")
    suspend fun getArticles(@Path("key") key: String): List<Article>

    @GET("27/{id}/{key}")
    suspend fun getArticleById(@Path("id") id: Int, @Path("key") key: String): List<Article>

    @GET("26/{key}")
    suspend fun getInterviewAsks(@Path("key") key: String): List<InterviewAsk>

    @GET("28/{key}")
    suspend fun getQuiz(@Path("key") key: String): List<Quiz>
}