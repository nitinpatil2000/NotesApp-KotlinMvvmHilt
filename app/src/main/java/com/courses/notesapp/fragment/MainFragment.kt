package com.courses.notesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.courses.notesapp.R
import com.courses.notesapp.adapter.NotesItemAdapter
import com.courses.notesapp.databinding.FragmentMainBinding
import com.courses.notesapp.util.ErrorHandling
import com.courses.notesapp.viewmodel.NoteViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment:Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val noteAdapter by lazy {
        NotesItemAdapter()
    }

    private val viewModel by viewModels<NoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        viewModel.getNotes()

        observeNoteState()

    }

    private fun setUpRecyclerView() {
        binding.noteList.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = noteAdapter
        }
    }

    private fun observeNoteState() {
        viewModel.noteLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false

            when (it) {
                is ErrorHandling.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is ErrorHandling.Error -> {
                    Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                }

                is ErrorHandling.Success -> {
                    noteAdapter.differ.submitList(it.data)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}