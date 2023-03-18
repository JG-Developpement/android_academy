package com.jgdeveloppement.android_academie.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Quiz(
    @SerializedName("quiz_id")
    @Expose
    val id: Int,
    @SerializedName("article_id")
    @Expose
    val articleId: Int,
    @SerializedName("quiz_ask")
    @Expose
    val quizAsk: String,
    @SerializedName("qcm_a")
    @Expose
    val qcmA: String,
    @SerializedName("qcm_b")
    @Expose
    val qcmB: String,
    @SerializedName("qcm_c")
    @Expose
    val qcmC: String,
    @SerializedName("qcm_d")
    @Expose
    val qcmD: String,
    @SerializedName("quiz_answer")
    @Expose
    val quizAnswer: String
)
