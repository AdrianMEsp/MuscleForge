package com.adrian.muscleforge.exercise.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.adrian.muscleforge.exercise.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Insert
    suspend fun addExercise(exercise: Exercise)

    @Query("SELECT * FROM exercise")
    fun getAllExercises(): Flow<List<Exercise>>

    @Delete
    suspend fun delete(exercise: Exercise)

    @Query(
        " SELECT * FROM exercise WHERE exerciseId NOT IN " +
                "(SELECT exerciseId FROM routine_exercise_cross_ref WHERE routineId = :routineId)"
    )
    suspend fun getUnassignedExercises(routineId: Long): List<Exercise>

    @Update
    suspend fun updateExercise(exercise: Exercise)
}