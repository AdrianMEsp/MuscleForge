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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrian.muscleforge.databinding.FragmentExerciseBinding
import com.adrian.muscleforge.exercise.adapter.ExerciseAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseFragment : Fragment() {

    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExerciseViewModel by viewModels()
    private lateinit var adapter: ExerciseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExerciseBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)

        adapter = ExerciseAdapter(emptyList(),
            onEditClick = { exercise -> editExercise(exercise) },
            onDeleteClick = { exercise -> deleteExercise(exercise) }
        )
        binding.exerciseRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.exerciseRecyclerView.adapter = adapter

        binding.fabAddExercise.setOnClickListener {
            showDialog()
        }

        // Observar datos de Room
        lifecycleScope.launchWhenStarted {
            viewModel.exercises.collect { exercises ->
                val sortedList = exercises.sortedBy { it.name } //ordena por nombre la lista de exercises
                adapter.updateList(sortedList)
            }
        }
    }

    //optimizar luego
    private fun editExercise(exercise: Exercise){
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

            if (repeats == 0){
                repeats = exercise.repetitions
            }
            if (weight.equals(0.0)){
                weight = exercise.weight
            }
            if (series == 0){
                series = exercise.series
            }

            if (name.isNotBlank()) {
                viewModel.addExercise(name, series, repeats, weight)
                viewModel.deleteExercise(exercise)
            } else {
                viewModel.addExercise(exercise.name, series, repeats, weight)
                viewModel.deleteExercise(exercise)
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun deleteExercise(exercise: Exercise){
        showDialogConfirm{ confirmed ->
            if (confirmed){
                viewModel.deleteExercise(exercise)
            }
        }
    }

    private fun showDialogConfirm(onResult: (Boolean) -> Unit){
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to delete this exercise?")
        builder.setPositiveButton("Accept"){_,_ ->
            onResult(true)
        }
        builder.setNegativeButton("Cancel") {dialog,_ ->
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
            val name = inputName.text.toString()
            val series = inputSeries.text.toString().toIntOrNull() ?: 0
            val repeats = inputRepeats.text.toString().toIntOrNull() ?: 0
            val weight = inputWeight.text.toString().toDoubleOrNull() ?: 0.0

            if (name.isNotBlank()) {
                viewModel.addExercise(name, series, repeats, weight)
            } else {
                Toast.makeText(requireContext(), "Name can't be empty", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}


