// TaskViewModel.kt
package com.example.todoapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {
    private val _tasks = MutableLiveData<MutableList<Task>>(mutableListOf())
    val tasks: LiveData<MutableList<Task>> get() = _tasks

    fun addTask(task: Task) {
        val currentList = _tasks.value ?: mutableListOf()
        currentList.add(task)
        _tasks.value = currentList // This triggers observers
    }

    fun deleteTask(task: Task) {
        val currentList = _tasks.value ?: return
        currentList.remove(task)
        _tasks.value = currentList
    }
}
