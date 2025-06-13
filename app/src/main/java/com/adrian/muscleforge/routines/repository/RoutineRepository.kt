package com.adrian.muscleforge.routines.repository

import com.adrian.muscleforge.exercise.Exercise
import com.adrian.muscleforge.exercise.dao.ExerciseDao
import com.adrian.muscleforge.relation.RoutineWithExercises
import com.adrian.muscleforge.routines.Routine
import com.adrian.muscleforge.routines.dao.RoutineDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoutineRepository @Inject constructor(
    private val routineDao: RoutineDao,
    private val exerciseDao: ExerciseDao
) {

    fun getAllRoutines(): Flow<List<Routine>> = routineDao.getAllRoutines()

    suspend fun removeExerciseFromRoutine(exerciseId: Long, routineId: Long) {
        routineDao.deleteExerciseFromRoutine(routineId, exerciseId)
    }

    suspend fun updateRoutine(routine: Routine){
        routineDao.updateRoutine(routine)
    }

    suspend fun deleteRoutine(routine: Routine){
        routineDao.delete(routine)
    }

    suspend fun updateExercise(exercise: Exercise) {
        exerciseDao.updateExercise(exercise)
    }

    suspend fun addRoutine(routine: Routine){
        routineDao.addRoutine(routine)
    }

    suspend fun getRoutineWithExercises(routineId: Long):
            RoutineWithExercises = routineDao.getRoutineWithExercises(routineId)


}