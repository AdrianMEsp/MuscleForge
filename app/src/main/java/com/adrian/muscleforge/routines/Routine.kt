package com.adrian.muscleforge.routines

import com.adrian.muscleforge.exercise.Exercise

data class Routine(
    val id: Int,
    val name: String,
    val exercises: MutableList<Exercise> = mutableListOf()
)
