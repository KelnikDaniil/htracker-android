package com.kelnik.htracker.domain.repository

import com.kelnik.htracker.domain.entity.Habit
import com.kelnik.htracker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    suspend fun addOrUpdateHabit(habit: Habit): Resource<Long>
    suspend fun removeHabit(habitId: Int): Resource<Unit>
    suspend fun getHabit(habitId: Int): Resource<Habit>
    suspend fun getHabitList(): Resource<Flow<List<Habit>>>
}