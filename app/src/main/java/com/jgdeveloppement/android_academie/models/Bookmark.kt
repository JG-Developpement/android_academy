package com.jgdeveloppement.android_academie.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark")
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    var articleId: Int,
    var page: Int)
