package com.adrian.muscleforge.exercise

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrian.muscleforge.databinding.FragmentExerciseBinding
import com.adrian.muscleforge.exercise.adapter.ExerciseAdapter
import com.adrian.muscleforge.relation.RoutineExerciseCrossRef
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
            onItemClick = { /* Puedes usarlo para selección visual si lo deseas */ },
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
                        Toast.makeText(requireContext(), "Selecciona al menos un ejercicio", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    lifecycleScope.launch {
                        selected.forEach {
                            viewModel.insertRoutineExerciseCrossRef(
                                RoutineExerciseCrossRef(routineId = id, exerciseId = it.exerciseId)
                            )
                        }
                        Toast.makeText(requireContext(), "Ejercicios añadidos a la rutina", Toast.LENGTH_SHORT).show()

                        // Recargar lista de no asignados
                        val updatedList = viewModel.getUnassignedExercises(id)
                        adapter.updateList(updatedList.sortedBy { it.name })
                    }
                }
            }

        } else {
            binding.btnConfirmSelection.visibility = View.GONE
            binding.fabAddExercise.setOnClickListener {
                showDialog()
            }
        }
        loadExercises()
    }

    private fun loadExercises() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (isSelectionMode && routineId != null) {
                    // Solo una vez obtienes la lista suspend
                    val unassigned = viewModel.getUnassignedExercises(routineId!!)
                    adapter.updateList(unassigned.sortedBy { it.name })
                } else {
                    // Aquí colectas el Flow mientras el lifecycle esté STARTED
                    viewModel.exercises.collect { exercises ->
                        adapter.updateList(exercises.sortedBy { it.name })
                    }
                }
            }
        }
    }


    //optimizar luego
    private fun editExercise(exercise: Exercise) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(exercise.name)

        // Creamos un layout vertical para los campos
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10) // márgenes opcionales

        val inputName = EditText(requireContext()).apply { hint = "Name of the Exercise" }
        val inputSeries = EditText(requireContext()).apply {
            hint = exercise.series.toString()
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        val inputRepeats = EditText(requireContext()).apply {
            hint = exercise.repetitions.toString()
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        val inputWeight = EditText(requireContext()).apply {
            hint = exercise.weight.toString()
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        layout.addView(inputName)
        layout.addView(inputSeries)
        layout.addView(inputRepeats)
        layout.addView(inputWeight)

        builder.setView(layout)

        builder.setPositiveButton("Save") { _, _ ->

//            makes the first char uppercase so the list is always sorted
            var name = inputName.text.toString().trim().replaceFirstChar { it.uppercaseChar() }

            var series = inputSeries.text.toString().toIntOrNull() ?: 0
            var repeats = inputRepeats.text.toString().toIntOrNull() ?: 0
            var weight = inputWeight.text.toString().toDoubleOrNull() ?: 0.0

            if (repeats == 0) {
                repeats = exercise.repetitions
            }
            if (weight.equals(0.0)) {
                weight = exercise.weight
            }
            if (series == 0) {
                series = exercise.series
            }
            if(name.isBlank()){
                name=exercise.name
            }
            val newExercise = Exercise(exercise.exerciseId,name, series, repeats, weight)
            viewModel.updateExercise(newExercise)
            }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun deleteExercise(exercise: Exercise) {
        showDialogConfirm { confirmed ->
            if (confirmed) {
                viewModel.deleteExercise(exercise)
            }
        }
    }

    private fun showDialogConfirm(onResult: (Boolean) -> Unit) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to delete this exercise?")
        builder.setPositiveButton("Accept") { _, _ ->
            onResult(true)
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
            onResult(false)
        }
        builder.show()
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("New Exercise")

        // Creamos un layout vertical para los campos
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10) // márgenes opcionales

        val inputName = EditText(requireContext()).apply { hint = "Name of the Exercise" }
        val inputSeries = EditText(requireContext()).apply {
            hint = "Number of series"
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        val inputRepeats = EditText(requireContext()).apply {
            hint = "Number of repeats"
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        val inputWeight = EditText(requireContext()).apply {
            hint = "Weight"
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        layout.addView(inputName)
        layout.addView(inputSeries)
        layout.addView(inputRepeats)
        layout.addView(inputWeight)

        builder.setView(layout)



        builder.setPositiveButton("Save") { _, _ ->
            val name = inputName.text.toString().trim().replaceFirstChar { it.uppercaseChar() }
            val series = inputSeries.text.toString().toIntOrNull() ?: 0
            val repeats = inputRepeats.text.toString().toIntOrNull() ?: 0
            val weight = inputWeight.text.toString().toDoubleOrNull() ?: 0.0

            if (name.isNotBlank()) {
                viewModel.addExercise(name, series, repeats, weight)
            } else {
                Toast.makeText(requireContext(), "Name can't be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


