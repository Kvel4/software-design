package ru.itmo.model

data class ListTasks(
    val listId: Int,
    val listName: String,
)

data class Task(
    val taskId: Int,
    val taskName: String,
    val isReady: Boolean,
)