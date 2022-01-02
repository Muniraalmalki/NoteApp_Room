package com.example.notesapproom.Activity

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapproom.Database.NoteDatabase
import com.example.notesapproom.Model.Note
import com.example.notesapproom.R
import com.example.notesapproom.Adapter.RecyclerViewAdapter
import com.example.notesapproom.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var recyclerView: RecyclerView
    lateinit var noteList: List<Note>

    private val noteDao by lazy { NoteDatabase.getDatabase(this).noteDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        addNote()
        getAllNote()
    }


    private fun getAllNote() {
        CoroutineScope(IO).launch {
            val data = async { noteDao.getAllNote() }.await()
            if (data.isNotEmpty()) {
                withContext(Main) {
                    noteList = data
                    recyclerView.adapter = RecyclerViewAdapter(noteList, this@MainActivity)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
            }
        }
    }

    private fun addNote() {
        binding.addButton.setOnClickListener {
            val note = binding.etNote.text.toString()
            CoroutineScope(IO).launch {
                noteDao.addNote(Note(0, note))
                getAllNote()
            }
            Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show()
        }
    }

    fun raiseDialog(pk: Int) {
        val dialogBuilder = AlertDialog.Builder(this)
        val updatedNote = EditText(this)
        updatedNote.hint = "Enter new text"
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("Save", DialogInterface.OnClickListener { _, _ ->
                editNote(pk, updatedNote.text.toString())
                recyclerView.adapter!!.notifyDataSetChanged()
                getAllNote()
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Update Note")
        alert.setView(updatedNote)
        alert.show()
    }

    private fun editNote(pk: Int, note: String) {
        CoroutineScope(IO).launch {
            noteDao.updateNote(Note(pk, note))
            getAllNote()
        }
        Toast.makeText(this,"Update Successfully",Toast.LENGTH_SHORT).show()
    }

    fun deleteNote(pk: Int) {
       CoroutineScope(IO).launch {
           noteDao.deleteNote(Note(pk,""))
           getAllNote()
       }
        Toast.makeText(this,"Delete Successfully",Toast.LENGTH_SHORT).show()
    }
}