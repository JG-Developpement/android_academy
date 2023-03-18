package com.jgdeveloppement.android_academie

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jgdeveloppement.android_academie.models.Bookmark
import com.jgdeveloppement.android_academie.room.AcademyDatabase
import com.jgdeveloppement.android_academie.room.BookmarkDao
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomTest {
    private lateinit var database: AcademyDatabase
    private lateinit var bookmarkDao: BookmarkDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AcademyDatabase::class.java).build()
        bookmarkDao = database.bookmarkDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertAndGetAllBookmark() = runBlocking {
        // Arrange
        val bookmark1 = Bookmark(1, 2, 1)
        val bookmark2 = Bookmark(2, 3, 1)
        bookmarkDao.insertBookmark(bookmark1)
        bookmarkDao.insertBookmark(bookmark2)

        // Act
        val bookmarks = bookmarkDao.getAllBookmark()

        // Assert
        MatcherAssert.assertThat(bookmarks.size, CoreMatchers.equalTo(2))
        MatcherAssert.assertThat(bookmarks[0].articleId, CoreMatchers.equalTo(2))
        MatcherAssert.assertThat(bookmarks[1].articleId, CoreMatchers.equalTo(3))
    }

    @Test
    fun testGetBookmarkByArticleId() = runBlocking {
        // Arrange
        val bookmark = Bookmark(1, 2, 1)
        bookmarkDao.insertBookmark(bookmark)

        // Act
        val result = bookmarkDao.getBookmarkByArticleId(2)

        // Assert
        MatcherAssert.assertThat(result, CoreMatchers.equalTo(bookmark))
    }

    @Test
    fun testDeleteBookmark() = runBlocking {
        // Arrange
        val bookmark = Bookmark(1, 2, 1)
        bookmarkDao.insertBookmark(bookmark)

        // Act
        bookmarkDao.deleteBookmark(2)
        val result = bookmarkDao.getBookmarkByArticleId(1)

        // Assert
        MatcherAssert.assertThat(result, CoreMatchers.equalTo(null))
    }
}