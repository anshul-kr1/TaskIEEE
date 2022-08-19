package com.example.android.task

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class AddNoteActivity : AppCompatActivity() {

    private lateinit var noteEditText: EditText
    private lateinit var addNoteButton: Button
    private lateinit var noteDao: NoteDao



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        noteEditText= findViewById(R.id.noteEditText)
        addNoteButton = findViewById(R.id.addNoteButton)
        noteDao = NoteDao()

        addNoteButton.setOnClickListener{
            val note = noteEditText.text.toString()
            if(note.isNotEmpty()){

                noteDao.addNote(note)


                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }

    }
}