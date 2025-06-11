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

        adapter = ExerciseAdapter(emptyList())
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


