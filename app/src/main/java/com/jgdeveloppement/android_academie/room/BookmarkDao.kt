package com.jgdeveloppement.android_academie.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jgdeveloppement.android_academie.models.Bookmark

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmark")
    fun getAllBookmark(): List<Bookmark>

    @Query("SELECT * FROM bookmark WHERE articleId = :articleId")
    fun getBookmarkByArticleId(articleId: Int): Bookmark

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBookmark(bookmark: Bookmark)

    @Query("DELETE FROM bookmark WHERE articleId = :articleId")
    fun deleteBookmark(articleId: Int)
}