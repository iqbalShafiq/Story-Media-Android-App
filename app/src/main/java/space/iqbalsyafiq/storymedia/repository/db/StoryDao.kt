package space.iqbalsyafiq.storymedia.repository.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import space.iqbalsyafiq.storymedia.model.Story

@Dao
interface StoryDao {
    // Insert story to database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStoryToFavorite(story: Story): Long

    // Get stories
    @Query("SELECT * FROM story")
    suspend fun getStories(): List<Story>

    // Get story by id
    @Query("SELECT * FROM story WHERE id = :storyId")
    suspend fun getStory(storyId: String): Story?

    // Delete story by Id
    @Query("DELETE FROM story WHERE id = :storyId")
    suspend fun deleteStory(storyId: String)

    // Delete story by Id
    @Query("DELETE FROM story")
    suspend fun deleteStories()
}