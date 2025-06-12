package com.adrian.muscleforge.routines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrian.muscleforge.R
import com.adrian.muscleforge.databinding.FragmentRoutineDetailBinding
import com.adrian.muscleforge.exercise.adapter.ExerciseAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutineDetailFragment : Fragment() {

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
        // Obtener ID de rutina desde SafeArgs
        val routineId = args.routineId

        adapter = ExerciseAdapter(
            emptyList(),
            onEditClick = {},
            onDeleteClick = {},
            onItemClick = {}
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // FAB para agregar ejercicios a esta rutina
        binding.fabAddExercise.setOnClickListener {
            val bundle = bundleOf(
                "isSelectionMode" to true,
                "routineId" to routineId
            )
            findNavController().navigate(R.id.exerciseFragment, bundle)
        }

        // Cargar ejercicios asignados
        viewModel.loadExercisesForRoutine(routineId)
        lifecycleScope.launchWhenStarted {
            viewModel.routineExercises.collect { exercises ->
                adapter.updateList(exercises.sortedBy { it.name })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
