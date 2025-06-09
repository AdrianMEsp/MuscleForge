package com.adrian.muscleforge.routines

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoutineRepository @Inject constructor(private val dao: RoutineDao) {
    suspend fun insert(routine: Routine) = dao.insertRoutine(routine)
    fun getAll(): Flow<List<Routine>> = dao.getAllRoutines()
}
