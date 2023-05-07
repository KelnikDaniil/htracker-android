package com.kelnik.htracker.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kelnik.htracker.data.local.model.EventNotificationDbModel

@Dao
interface EventNotificationDao {
    @Query("SELECT * FROM EventNotificationDbModel WHERE id = :eventNotificationId")
    suspend fun get(eventNotificationId: Int): EventNotificationDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(eventNotificationDbModel: EventNotificationDbModel)

    @Query("DELETE FROM EventNotificationDbModel WHERE id = :eventNotificationId")
    suspend fun delete(eventNotificationId: Int)

    @Query("DELETE FROM EventNotificationDbModel WHERE habit_id = :habitId")
    suspend fun deleteForHabit(habitId: Int)

    @Query("SELECT * FROM EventNotificationDbModel WHERE habit_id = :habitId ORDER BY date ASC")
    suspend fun getForHabit(habitId: Int): List<EventNotificationDbModel>

    @Query("DELETE FROM EventNotificationDbModel WHERE habit_id = :habitId AND is_done = 0 AND date >= :date")
    suspend fun deleteLaterThanDateInclusiveWhereIsDoneFalseForHabit(habitId: Int, date: Long)

    @Query("SELECT * FROM EventNotificationDbModel WHERE habit_id = :habitId AND is_done = 0 AND date >= :date")
    suspend fun getLaterThanDateInclusiveWhereIsDoneFalseForHabit(habitId: Int, date: Long): List<EventNotificationDbModel>

    @Query("SELECT * FROM EventNotificationDbModel WHERE habit_id = :habitId AND date = :date")
    suspend fun getForHabitAndDate(habitId: Int, date: Long): EventNotificationDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg eventNotificationDbModel: EventNotificationDbModel)

    @Query("SELECT * FROM EventNotificationDbModel ORDER BY date ASC")
    fun getAllLiveData(): LiveData<List<EventNotificationDbModel>>

    @Query("SELECT * FROM EventNotificationDbModel ORDER BY date ASC")
    fun getAll(): List<EventNotificationDbModel>
}