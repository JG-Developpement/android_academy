package com.jgdeveloppement.android_academie

import com.jgdeveloppement.android_academie.models.Article
import com.jgdeveloppement.android_academie.models.InterviewAsk
import com.jgdeveloppement.android_academie.models.Quiz
import com.jgdeveloppement.android_academie.retrofit.ApiService
import com.jgdeveloppement.android_academie.utils.Categories
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RetrofitTest {

    @Mock
    lateinit var apiService: ApiService

    private val key = BuildConfig.API_KEY

    @Test
    fun getArticles() {
        runBlocking {
            val articles = listOf(Article(1, Categories.QCM_KOTLIN, "QCM_KOTLIN", "22/03/2023"),
                Article(2, Categories.INTERVIEW_ANDROID, "INTERVIEW_ANDROID", "24/03/2023"))
            Mockito.`when`(apiService.getArticles(anyString())).thenReturn(articles)

            val result = apiService.getArticles(key)

            assertEquals(articles, result)
        }
    }

    @Test
    fun getArticleById() {
        runBlocking {
            val article = Article(1, Categories.QCM_KOTLIN, "QCM_KOTLIN", "22/03/2023")
            Mockito.`when`(apiService.getArticleById(anyInt(), anyString())).thenReturn(listOf(article))

            val result = apiService.getArticleById(1, key)

            assertEquals(article, result.first())
        }
    }

    @Test
    fun getInterviewAsks() {
        runBlocking {
            val interviewAsks = listOf(InterviewAsk(1, 1, "ask", "answer", 1),
                InterviewAsk(2, 1, "ask1", "answer1", 2))
            Mockito.`when`(apiService.getInterviewAsks(anyString())).thenReturn(interviewAsks)

            val result = apiService.getInterviewAsks(key)

            assertEquals(interviewAsks, result)
        }
    }

    @Test
    fun getQuiz() {
        runBlocking {
            val quiz = listOf(Quiz(1, 1, "quizAsk", "qcmA", "qcmB", "qcmC", "qcmD", "quizAnswer"),
                Quiz(2, 1, "quizAsk1", "qcmA1", "qcmB1", "qcmC1", "qcmD1", "quizAnswer1"))
            Mockito.`when`(apiService.getQuiz(anyString())).thenReturn(quiz)

            val result = apiService.getQuiz(key)

            assertEquals(quiz, result)
        }
    }
}