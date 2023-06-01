package com.kelnik.htracker.data.repository

import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import com.kelnik.htracker.data.local.dao.HabitDao
import com.kelnik.htracker.data.local.model.HabitDbModel
import com.kelnik.htracker.domain.entity.Habit
import com.kelnik.htracker.domain.mapper.Mapper
import com.kelnik.htracker.domain.repository.HabitRepository
import com.kelnik.htracker.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val mapper: Mapper<Habit, HabitDbModel>
) : HabitRepository {
    override suspend fun addOrUpdateHabit(habit: Habit): Resource<Long> =
        withContext(Dispatchers.IO) {
            val item = mapper.mapItemToDbModel(habit)
            val id = habitDao.insert(item)
            Resource.Success(id)
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
    override suspend fun getHabitList(): Resource<Flow<List<Habit>>> {
        val result = habitDao.getAll()
        val flow = result.asFlow().map { it.map { mapper.mapDbModelToItem(it) } }
        return Resource.Success(flow)
    }
}