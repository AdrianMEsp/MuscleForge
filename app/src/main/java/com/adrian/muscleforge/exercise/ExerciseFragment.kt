package com.adrian.muscleforge.exercise

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrian.muscleforge.databinding.FragmentExerciseBinding
import com.adrian.muscleforge.exercise.adapter.ExerciseAdapter
import com.adrian.muscleforge.relation.RoutineExerciseCrossRef
import com.adrian.muscleforge.utils.DialogHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExerciseFragment : Fragment() {

    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExerciseViewModel by viewModels()
    private lateinit var adapter: ExerciseAdapter

    private var isSelectionMode = false
    private var routineId: Long? = null

    //for the searchView
    private var currentExercises: List<Exercise> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            isSelectionMode = it.getBoolean("isSelectionMode", false)
            routineId = it.getLong("routineId", -1).takeIf { id -> id != -1L }
        }

        adapter = ExerciseAdapter(
            exercises = emptyList(),
            onEditClick = { exercise -> if (!isSelectionMode) editExercise(exercise) },
            onDeleteClick = { exercise -> if (!isSelectionMode) deleteExercise(exercise) },
            onItemClick = { },
            isSelectionMode = isSelectionMode
        )

        binding.exerciseRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.exerciseRecyclerView.adapter = adapter

        if (isSelectionMode) {
            binding.fabAddExercise.visibility = View.GONE
            binding.btnConfirmSelection.visibility = View.VISIBLE

            binding.btnConfirmSelection.setOnClickListener {
                routineId?.let { id ->
                    val selected = adapter.getSelectedExercises()
                    if (selected.isEmpty()) {
                        return@setOnClickListener
                    }

                    lifecycleScope.launch {
                        selected.forEach {
                            viewModel.insertRoutineExerciseCrossRef(
                                RoutineExerciseCrossRef(routineId = id, exerciseId = it.exerciseId)
                            )
                        }

                        // Recargar lista de no asignados
                        val updatedList = viewModel.getUnassignedExercises(id)
                        adapter.updateList(updatedList.sortedBy { it.name })
                    }
                }
            }

        } else {
            binding.btnConfirmSelection.visibility = View.GONE
            binding.fabAddExercise.setOnClickListener {
                showDialogCreateExercise()
            }
        }
        loadExercises()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = currentExercises.filter {
                    it.name.contains(newText.orEmpty(), ignoreCase = true)
                }
                adapter.updateList(filteredList)
                return true
            }
        })
    }

    private fun loadExercises() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (isSelectionMode && routineId != null) {
                    // Solo una vez obtienes la lista suspend
                    val unassigned = viewModel.getUnassignedExercises(routineId!!)
                    currentExercises = unassigned.sortedBy { it.name } // for the searchView
                    adapter.updateList(unassigned.sortedBy { it.name })
                } else {
                    // Aquí colectas el Flow mientras el lifecycle esté STARTED
                    viewModel.exercises.collect { exercises ->
                        currentExercises = exercises.sortedBy { it.name } // for the searchView
                        adapter.updateList(exercises.sortedBy { it.name })
                    }
                }
            }
        }
    }

    private fun editExercise(exercise: Exercise) {
        DialogHelper.showDialogEditRoutine(requireContext(),exercise) {
            updatedExercise -> viewModel.updateExercise(updatedExercise)
        }
    }

    private fun deleteExercise(exercise: Exercise) {
        DialogHelper.showDialogConfirmDeleteExercise(requireContext()) { confirmed ->
            if (confirmed) {
                viewModel.deleteExercise(exercise)
            }
        }
    }

    private fun showDialogCreateExercise() {
        DialogHelper.showDialogCreateExercise(requireContext()) { exercise ->
            exercise?.let { viewModel.addExercise(exercise) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


