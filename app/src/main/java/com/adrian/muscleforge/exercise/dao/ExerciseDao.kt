package com.adrian.muscleforge.exercise.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.adrian.muscleforge.exercise.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Insert
    suspend fun addExercise(exercise: Exercise)

    @Query("SELECT * FROM exercises")
    fun getAllExercises(): Flow<List<Exercise>>

    @Delete
    suspend fun delete(exercise: Exercise)
}