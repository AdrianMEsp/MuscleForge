package com.adrian.muscleforge.relation

import androidx.room.Embedded
import com.adrian.muscleforge.exercise.Exercise

data class ExerciseWithPosition(
    @Embedded val exercise : Exercise,
    val position: Int
)
