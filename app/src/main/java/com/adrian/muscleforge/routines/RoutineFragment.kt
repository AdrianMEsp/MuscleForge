package com.adrian.muscleforge.routines

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrian.muscleforge.databinding.FragmentRoutinesBinding
import com.adrian.muscleforge.routines.adapter.RoutineAdapter

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutineFragment : Fragment() {

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
        adapter = RoutineAdapter(emptyList())
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

    //to create a new routine only with name
    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Nueva rutina")

        val input = EditText(requireContext())
        input.hint = "Nombre de la rutina"
        builder.setView(input)

        builder.setPositiveButton("Guardar") { _, _ ->
            val name = input.text.toString()
            if (name.isNotBlank()) {
                viewModel.addRoutine(name)
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
