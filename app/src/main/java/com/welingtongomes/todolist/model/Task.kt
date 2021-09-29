package com.welingtongomes.todolist.model

data class Task(
        val title: String,
        val data: String,
        val hora: String,
        val id: Int = 0
)