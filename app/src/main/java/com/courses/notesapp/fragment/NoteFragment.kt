package com.courses.notesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.courses.notesapp.R
import com.courses.notesapp.databinding.FragmentNoteBinding
import com.courses.notesapp.models.note.NoteRequest
import com.courses.notesapp.models.note.NoteResponse
import com.courses.notesapp.util.ErrorHandling
import com.courses.notesapp.viewmodel.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment(R.layout.fragment_note) {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    //for to get the bundle value you need to set up the args in the navigation xml file firstly.
    private val args by navArgs<NoteFragmentArgs>()
    private var note: NoteResponse? = null

    private val viewModel by viewModels<NoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitialData()
        setUpHandlers()
        setUpObservers()

    }

    private fun setUpHandlers() {
        binding.btnDelete.setOnClickListener {
            note?.let {
                viewModel.deleteNote(it!!._id)
            }
        }

        binding.btnSubmit.setOnClickListener {
            val title = binding.txtTitle.text.toString()
            val description = binding.txtDescription.text.toString()
            val noteRequest = NoteRequest(title, description)

            if (note == null) {
                viewModel.createNote(noteRequest)
            } else {
                viewModel.updateNote(note!!._id, noteRequest)
            }
        }
    }

    private fun setUpObservers() {
        viewModel.statusLiveData.observe(viewLifecycleOwner) {
            binding.btnSubmit.revertAnimation()
            when (it) {
                is ErrorHandling.Error -> {
                    Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                }

                is ErrorHandling.Loading -> {
                    binding.btnSubmit.startAnimation()
                }

                is ErrorHandling.Success -> {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun setInitialData() {
        note = args.note

        if (note != null) {
            note?.let {
                binding.apply {
                    txtTitle.setText(it.title)
                    txtDescription.setText(it.description)
                }
            }
        } else {
            binding.apply {
                addEditText.text = "Add Note"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}