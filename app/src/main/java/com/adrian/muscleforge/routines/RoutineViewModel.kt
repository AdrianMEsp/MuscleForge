package com.adrian.muscleforge.routines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrian.muscleforge.routines.dao.RoutineDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val routineDao: RoutineDao
) : ViewModel() {

    private val _routines = MutableStateFlow<List<Routine>>(emptyList())
    val routines: StateFlow<List<Routine>> = _routines.asStateFlow()

    init {
        loadRoutines()
    }

    private fun loadRoutines() {
        viewModelScope.launch {
            routineDao.getAllRoutines()
                .collect { list ->
                    _routines.value = list
                }
        }
    }

    fun addRoutine(name: String) {
        viewModelScope.launch {
            val newRoutine = Routine(name = name)
            routineDao.insertRoutine(newRoutine)
            // No hace falta volver a cargar manualmente: el Flow se actualiza solo
        }
    }
}

