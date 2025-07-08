// TaskViewModel.kt
package com.example.todoapp

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {
    private val _tasks = MutableLiveData<MutableList<Task>>(mutableListOf())
    val tasks: LiveData<MutableList<Task>> get() = _tasks

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("tasks")

    init {
        // Called once when ViewModel is created
        loadTasks()
    }


    fun loadTasks() {
        collection.addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                val taskList = snapshot.documents.mapNotNull { it.toObject(Task::class.java) }
                _tasks.value = taskList.toMutableList()
            }
        }
    }

    fun addTask(task: Task) {
        collection.add(task)
            .addOnSuccessListener {
                // Only load tasks *after* Firestore confirms the task was added
                loadTasks()
            }
            .addOnFailureListener { e ->
                Log.e("TaskViewModel", "Failed to add task", e)
            }
    }

    fun deleteTask(task: Task) {
        collection.whereEqualTo("description", task.description)
            .get()
            .addOnSuccessListener { snapshot ->
                for (doc in snapshot) {
                    collection.document(doc.id).delete()
                }
                loadTasks()
            }
    }
}
