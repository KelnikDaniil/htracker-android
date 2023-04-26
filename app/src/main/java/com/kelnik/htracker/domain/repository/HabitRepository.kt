package com.kelnik.htracker.domain.repository

import com.kelnik.htracker.domain.entity.Habit
import com.kelnik.htracker.utils.Resource

interface HabitRepository {
    suspend fun addOrUpdateHabit(habit: Habit): Resource<Unit>
    suspend fun removeHabit(habitId: Int): Resource<Unit>
    suspend fun getHabit(habitId: Int): Resource<Habit>
    suspend fun getHabitList(): Resource<List<Habit>>
}