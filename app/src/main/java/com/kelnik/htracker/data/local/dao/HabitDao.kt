package com.kelnik.htracker.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kelnik.htracker.data.local.model.HabitDbModel

@Dao
interface HabitDao {
    @Query("SELECT * FROM HabitDbModel WHERE id = :habitId")
    suspend fun get(habitId: Int): HabitDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habitDbModel: HabitDbModel)

    @Query("DELETE FROM HabitDbModel WHERE id = :habitId")
    suspend fun delete(habitId: Int)

    @Query("SELECT * FROM HabitDbModel ORDER BY created_at ASC")
    fun getAll(): LiveData<List<HabitDbModel>>
}