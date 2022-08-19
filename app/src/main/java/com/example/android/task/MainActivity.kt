package com.example.android.task

import android.app.DownloadManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var noteDao: NoteDao
    private lateinit var auth:FirebaseAuth
    private lateinit var adaptor: RVAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        fab = findViewById(R.id.fab)

        noteDao= NoteDao()
        auth = Firebase.auth

        fab.setOnClickListener{

            val intent = Intent(this,AddNoteActivity::class.java)
            startActivity(intent)
        }

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        val noteCollection = noteDao.noteCollection
        val currentUserId = auth.currentUser!!.uid

        val query = noteCollection.whereEqualTo("uid",currentUserId).orderBy("text",
            Query.Direction.ASCENDING)

        val recyclerViewOption = FirestoreRecyclerOptions.Builder<Note>().setQuery(query,Note::class.java).build()

        adaptor  = RVAdaptor(recyclerViewOption)
        recyclerView.adapter = adaptor

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                adaptor.deleteNote(position)
            }

        }).attachToRecyclerView(recyclerView)

    }

    override fun onStart() {
        super.onStart()
        adaptor.startListening()
    }

    override fun onStop() {
        super.onStop()
        adaptor.stopListening()
    }

}