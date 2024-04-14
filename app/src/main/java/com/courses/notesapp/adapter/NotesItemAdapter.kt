package com.courses.notesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.courses.notesapp.databinding.NoteItemBinding
import com.courses.notesapp.models.note.NoteResponse

class NotesItemAdapter : RecyclerView.Adapter<NotesItemAdapter.NotesItemViewHolder>() {
    inner class NotesItemViewHolder(private val binding: NoteItemBinding) :
        ViewHolder(binding.root) {
        fun bind(noteList: NoteResponse) {
            binding.apply {
                title.text = noteList.title
                desc.text = noteList.description
            }
        }

    }

    private val _differItemCallback = object : DiffUtil.ItemCallback<NoteResponse>() {
        override fun areItemsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, _differItemCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesItemViewHolder {
        return NotesItemViewHolder(
            NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotesItemViewHolder, position: Int) {
        val noteList = differ.currentList[position]
        holder.bind(noteList)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}