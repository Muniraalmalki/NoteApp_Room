package com.example.notesapproom.Adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapproom.Activity.MainActivity
import com.example.notesapproom.Model.Note
import com.example.notesapproom.databinding.ItemRowBinding

class RecyclerViewAdapter( private var noteList: List<Note>,private var activity: MainActivity): RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemRowBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val notes = noteList[position]
        holder.binding.apply {
            tvNote.text = notes.note

            editNote.setOnClickListener {
                    activity.raiseDialog(notes.pk)
                }
            deleteNote.setOnClickListener {
                    val builder = AlertDialog.Builder(holder.itemView.context)
                    builder.setTitle("Are you sure want to delete this note?")
                    builder.setPositiveButton("Delete") { _, _ ->
                        activity.deleteNote(notes.pk)
                    }
                    builder.setNegativeButton("Cancel") { _, _ -> }
                    builder.show()
                }
        }
    }
        override fun getItemCount(): Int {
            return noteList.size
        }

        fun updateRV(noteList: List<Note>) {
            this.noteList = noteList
            notifyDataSetChanged()
        }
    }