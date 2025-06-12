package com.adrian.muscleforge.exercise

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val exerciseId: Long=0,
    val name: String,
    val series: Int,
    val repetitions: Int,
    val weight: Double
    )

