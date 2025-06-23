package com.adrian.muscleforge.exercise.repository

import com.adrian.muscleforge.exercise.Exercise
import com.adrian.muscleforge.exercise.dao.ExerciseDao
import javax.inject.Inject

class ExerciseRepository @Inject constructor(private val dao: ExerciseDao){

    suspend fun delete(exercise: Exercise) = dao.delete(exercise)

}