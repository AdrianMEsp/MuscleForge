package com.adrian.muscleforge.routines

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrian.muscleforge.R
import com.adrian.muscleforge.databinding.FragmentRoutineDetailBinding
import com.adrian.muscleforge.exercise.Exercise
import com.adrian.muscleforge.exercise.adapter.ExerciseAdapter
import com.adrian.muscleforge.utils.DialogHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutineDetailFragment : Fragment() {

    val MESSAGE_DELETE_CONFIRMATION = "Are you sure you want to delete this exercise?"

    private var _binding: FragmentRoutineDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RoutineViewModel by viewModels()
    private lateinit var adapter: ExerciseAdapter
    private var routineId: Long = -1L

    // Safe Args
    private val args: RoutineDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoutineDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        gets ID and NAME from routine from SafeArgs(whitin main_grafp)
        val routineId = args.routineId
        val routineName = args.routineName

        binding.tvRoutineNameInRoutine.text = routineName

        adapter = ExerciseAdapter(
            emptyList(),
            onEditClick = { exercise -> editExercise(exercise,routineId) },
            onDeleteClick = { exercise -> deleteExercise(exercise,routineId) },
            onItemClick = {}
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // FAB(Floating action button) to add exercises to this routine
        binding.fabAddExercise.setOnClickListener {
            val bundle = bundleOf(
                "isSelectionMode" to true,
                "routineId" to routineId
            )
            findNavController().navigate(R.id.exerciseFragment, bundle)
        }

//        Load asigned exercises for this routine
        viewModel.loadExercisesForRoutine(routineId)
        lifecycleScope.launchWhenStarted {
            viewModel.exercisesInRoutine.collect { exercises ->
                adapter.updateList(exercises.sortedBy { it.name })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun deleteExercise(exercise: Exercise, routineId: Long){
//        object in Utils, it's a personal dialog
        DialogHelper.showDialogConfirm(requireContext(), MESSAGE_DELETE_CONFIRMATION) { confirmed ->
            if (confirmed){
                viewModel.deleteExerciseFromRoutine(exercise.exerciseId, routineId)
            }
        }
    }

    private fun editExercise(exercise: Exercise, routineId: Long) {
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
            if (name.isBlank()) {
                name = exercise.name
            }
            val newExercise = Exercise(exercise.exerciseId, name, series, repeats, weight)

            viewModel.editExercise(newExercise,routineId)
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
