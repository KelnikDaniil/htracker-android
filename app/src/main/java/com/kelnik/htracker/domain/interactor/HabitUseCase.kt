package com.kelnik.htracker.domain.interactor

import com.kelnik.htracker.domain.entity.Habit
import com.kelnik.htracker.domain.repository.HabitRepository
import com.kelnik.htracker.utils.Resource
import javax.inject.Inject

class HabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend fun addOrUpdateHabit(habit: Habit): Resource<Unit> =
        habitRepository.addOrUpdateHabit(habit)

    suspend fun removeHabit(habitId: Int): Resource<Unit> =
        habitRepository.removeHabit(habitId)

    suspend fun getHabit(habitId: Int): Resource<Habit> =
        habitRepository.getHabit(habitId)

    suspend fun getHabitList(): Resource<List<Habit>> =
        habitRepository.getHabitList()
}