package com.adrian.muscleforge.routines

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "routines")
data class Routine(
    @PrimaryKey(autoGenerate = true) val id:Int=0,
    val name: String,
    val isChecked: Boolean = false //cuando se crea se guarda sin check
)
