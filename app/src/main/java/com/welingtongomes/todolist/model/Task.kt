package com.welingtongomes.todolist.model

data class Task(
        var title: String,
        val date: String,
        val hour: String,
        val id: Int
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}