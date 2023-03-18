package com.jgdeveloppement.android_academie.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InterviewAsk(
    @SerializedName("interview_ask_id")
    @Expose
    val id: Int,
    @SerializedName("article_id")
    @Expose
    val articleId: Int,
    @SerializedName("interview_question")
    @Expose
    val ask: String,
    @SerializedName("interview_answer")
    @Expose
    val answer: String,
    @SerializedName("page_number")
    @Expose
    val page: Int
    )
