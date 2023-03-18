package com.jgdeveloppement.android_academie.room

import com.jgdeveloppement.android_academie.models.Bookmark

class AcademyHelper(private val bookmarkDao: BookmarkDao): BookmarkDao {

    override fun getAllBookmark(): List<Bookmark> = bookmarkDao.getAllBookmark()
    override fun getBookmarkByArticleId(articleId: Int) = bookmarkDao.getBookmarkByArticleId(articleId)
    override fun insertBookmark(bookmark: Bookmark) = bookmarkDao.insertBookmark(bookmark)
    override fun deleteBookmark(articleId: Int) = bookmarkDao.deleteBookmark(articleId)
}