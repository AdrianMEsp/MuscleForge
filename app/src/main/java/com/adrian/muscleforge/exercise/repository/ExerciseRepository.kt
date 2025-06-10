package com.adrian.muscleforge.exercise.repository

import com.adrian.muscleforge.exercise.Exercise
import com.adrian.muscleforge.exercise.dao.ExerciseDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExerciseRepository @Inject constructor(private val dao: ExerciseDao){
    suspend fun insert(exercise: Exercise) = dao.addExercise(exercise)

    fun getAll(): Flow<List<Exercise>> = dao.getAllExercises()

    suspend fun delete(exercise: Exercise) = dao.delete(exercise)

}