package com.adrian.muscleforge.routines.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.adrian.muscleforge.exercise.Exercise
import com.adrian.muscleforge.relation.ExerciseWithPosition
import com.adrian.muscleforge.relation.RoutineExerciseCrossRef
import com.adrian.muscleforge.relation.RoutineWithExercises
import com.adrian.muscleforge.routines.Routine
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Insert
    suspend fun addRoutine(routine: Routine)

    @Query("SELECT * FROM routines")
    fun getAllRoutines(): Flow<List<Routine>>

    @Update
    suspend fun updateRoutine(routine: Routine)

    @Delete
    suspend fun delete(routine: Routine)

    @Insert
    suspend fun insertExercise(exercise: Exercise): Long

    @Insert
    suspend fun insertRoutineExerciseCrossRef(crossRef: RoutineExerciseCrossRef)

    @Transaction
    @Query("SELECT * FROM routines " +
            "WHERE routineId = :id")
    suspend fun getRoutineWithExercises(id: Long): RoutineWithExercises

    @Query("DELETE FROM routine_exercise_cross_ref " +
            "WHERE routineId = :routineId AND exerciseId = :exerciseId")
    suspend fun deleteExerciseFromRoutine(routineId: Long, exerciseId: Long)

    @Query("""
    SELECT e.*, rec.position 
    FROM exercise e
    INNER JOIN routine_exercise_cross_ref rec 
    ON e.exerciseId = rec.exerciseId
    WHERE rec.routineId = :routineId
    ORDER BY rec.position ASC
""")
    suspend fun getExercisesWithPositionForRoutine(routineId: Long): List<ExerciseWithPosition>


    @Query("""
    UPDATE routine_exercise_cross_ref
    SET position = :position
    WHERE routineId = :routineId AND exerciseId = :exerciseId
""")
    suspend fun updatePosition(routineId: Long, exerciseId: Long, position: Int)


    @Query("""
    SELECT COUNT(*) 
    FROM routine_exercise_cross_ref 
    WHERE routineId = :routineId
    """)
    suspend fun getExerciseCountInRoutine(routineId: Long): Int


    @Query("""
    UPDATE routine_exercise_cross_ref
    SET position = :newPosition
    WHERE routineId = :routineId AND exerciseId = :exerciseId
""")
    suspend fun updateExercisePositionInRoutine(
        routineId: Long,
        exerciseId: Long,
        newPosition: Int
    )



}