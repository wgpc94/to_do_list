package com.welingtongomes.todolist.datasource

import com.welingtongomes.todolist.model.Task

object TaskDataSource{
    private val list = mutableListOf<Task>()

    fun getList() = list

    fun insertTask(task: Task){
        if (task.id == 0) {
            list.add(task.copy(id = list.size + 1))
        }
        else{
            list.remove(task)
            list.add(task)
        }

    }

    fun findById(taskId : Int) = list.find { it.id == taskId }
    fun remover(it: Task) {
        list.remove(it)
    }
}