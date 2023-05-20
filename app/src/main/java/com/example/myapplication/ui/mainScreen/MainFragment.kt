package com.example.myapplication.ui.mainScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.NoteAdapter
import com.example.myapplication.databinding.FragmentMainBinding
import com.example.myapplication.models.NoteResponse
import com.example.myapplication.utils.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()
    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        adapter = NoteAdapter(::onNoteClicked)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObserver()
        noteViewModel.getNotes()
        binding.noteList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = adapter
        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }
    }

    private fun bindObserver() {
        noteViewModel.noteLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Error -> Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()
                is NetworkResult.Loading -> binding.progressBar.isVisible = true
                is NetworkResult.Success -> {
                    adapter.submitList(it.data)
                }
            }
        }
    }

    private fun onNoteClicked(note: NoteResponse) {
        val bundle = Bundle()
        bundle.putString("note", Gson().toJson(note))
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}