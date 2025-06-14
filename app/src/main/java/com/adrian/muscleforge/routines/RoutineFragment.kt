package com.adrian.muscleforge.routines


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrian.muscleforge.R
import com.adrian.muscleforge.databinding.FragmentRoutinesBinding
import com.adrian.muscleforge.routines.adapter.RoutineAdapter
import com.adrian.muscleforge.utils.DialogHelper

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutineFragment : Fragment() {

    val MESAGGE_DELETE_ROUTINE= "Are you sure you want to delete this routine?"

    private var _binding: FragmentRoutinesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RoutineViewModel by viewModels()

    private lateinit var adapter: RoutineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoutinesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = RoutineAdapter(
            emptyList(),
            onCheckedChanged = { routine -> viewModel.updateRoutine(routine) },

            // Ver ejercicios de la rutina
            onRoutineClick = { routine ->
                val action = RoutineFragmentDirections
                    .actionRoutineToDetail(
                        routineId = routine.routineId,
                        routineName = routine.name
                    )
                findNavController().navigate(action)
            },

            // Eliminar rutina
            onDeleteClick = { routine ->
                DialogHelper.showDialogConfirm(requireContext(), MESAGGE_DELETE_ROUTINE) { confirmed ->
                    if (confirmed) {
                        viewModel.deleteRoutine(routine)
                    }
                }
            },

            // Agregar ejercicios a la rutina
            onAddExercisesClick = { routine ->
                val bundle = bundleOf(
                    "isSelectionMode" to true,
                    "routineId" to routine.routineId
                )
                findNavController().navigate(R.id.exerciseFragment, bundle)
            }
        )

        binding.routineRecyclerView.adapter = adapter
        binding.routineRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.createNewRoutine.setOnClickListener {
            showDialog()
        }

        // Observar rutinas
        lifecycleScope.launchWhenStarted {
            viewModel.routines.collect { routines ->
                adapter.updateList(routines)
            }
        }
    }

    private fun showDialog(){
        DialogHelper.showDialogCreateRoutine(requireContext()) {
            name ->
            name?.let {
                viewModel.addRoutine(name)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
