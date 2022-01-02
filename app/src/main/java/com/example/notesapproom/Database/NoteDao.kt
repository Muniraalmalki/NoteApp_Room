package com.example.notesapproom.Database

import androidx.room.*
import com.example.notesapproom.Model.Note

@Dao
interface NoteDao {

    @Insert
    suspend fun addNote(note: Note)

    @Query("SELECT * FROM Note ORDER BY pk ASC")
    fun getAllNote():List<Note>

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}