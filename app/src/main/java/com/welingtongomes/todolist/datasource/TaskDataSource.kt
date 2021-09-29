package com.welingtongomes.todolist.datasource

import com.welingtongomes.todolist.model.Task

object TaskDataSource{
    private val list = arrayListOf<Task>()

    fun getList(): ArrayList<Task> {
        return list
    }

    fun insertTask(task: Task){
        list.add(task.copy(id = list.size + 1))
    }
}