package com.adrian.muscleforge.relation

import androidx.room.Entity
import androidx.room.ForeignKey
import com.adrian.muscleforge.exercise.Exercise
import com.adrian.muscleforge.routines.Routine

@Entity(
    tableName = "routine_exercise_cross_ref",
    primaryKeys = ["routineId", "exerciseId"],
    foreignKeys = [
        ForeignKey(
            entity = Routine::class,
            parentColumns = ["routineId"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["exerciseId"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RoutineExerciseCrossRef(
    val routineId: Long,
    val exerciseId: Long
)
