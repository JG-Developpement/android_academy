package com.jgdeveloppement.android_academie.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jgdeveloppement.android_academie.utils.Categories


data class Article(
    @SerializedName("article_id")
    @Expose
    val id: Int,
    @SerializedName("article_categories")
    @Expose
    val categories: Categories,
    @SerializedName("article_title")
    @Expose
    val title: String,
    @SerializedName("article_date")
    @Expose
    val date: String
    )

