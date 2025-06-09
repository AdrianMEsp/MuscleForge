package com.adrian.muscleforge.routines

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrian.muscleforge.R
import com.adrian.muscleforge.databinding.FragmentRoutinesBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutineFragment : Fragment() {

    private var _binding: FragmentRoutinesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RoutineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoutinesBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        binding.createNewRoutine.setOnClickListener { showDialog() }
    }

    private fun setupListeners() {
        binding.createNewRoutine.setOnClickListener { showDialog() }
    }

    private fun setupRecyclerView() {
        adapter = RoutineAdapter()
        binding.rvRoutines.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRoutines.adapter = adapter

        adapter.submitList(
            listOf("Rutina dia martes","Rutina dia miercoles","Rutina para casa")
        )
    }

    private fun RoutineFragment.showDialog() {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null   // evita memory leaks
    }


}


