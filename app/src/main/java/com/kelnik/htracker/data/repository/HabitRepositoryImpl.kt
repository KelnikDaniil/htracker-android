package com.kelnik.htracker.data.repository

import com.kelnik.htracker.data.local.dao.HabitDao
import com.kelnik.htracker.data.local.model.HabitDbModel
import com.kelnik.htracker.domain.entity.Habit
import com.kelnik.htracker.domain.mapper.Mapper
import com.kelnik.htracker.domain.repository.HabitRepository
import com.kelnik.htracker.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val mapper: Mapper<Habit, HabitDbModel>
) : HabitRepository {
    override suspend fun addOrUpdateHabit(habit: Habit): Resource<Unit> =
        withContext(Dispatchers.IO) {
            val item = mapper.mapItemToDbModel(habit)
            habitDao.insert(item)
            Resource.Success(Unit)
        }

    override suspend fun removeHabit(habitId: Int): Resource<Unit> =
        withContext(Dispatchers.IO) {
            try {
                habitDao.delete(habitId)
                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Failure(NoSuchElementException())
            }
        }

    override suspend fun getHabit(habitId: Int): Resource<Habit> = withContext(Dispatchers.IO) {
        val result = habitDao.get(habitId)
        if (result == null) {
            Resource.Failure(NoSuchElementException())
        } else {
            Resource.Success(mapper.mapDbModelToItem(result))
        }
    }

    override suspend fun getHabitList(): Resource<List<Habit>> = withContext(Dispatchers.IO) {
        val result = habitDao.getAll()
        Resource.Success(result.map { mapper.mapDbModelToItem(it) })
    }
}