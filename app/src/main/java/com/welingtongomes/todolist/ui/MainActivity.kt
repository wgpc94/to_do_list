package com.welingtongomes.todolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.welingtongomes.todolist.databinding.ActivityMainBinding
import com.welingtongomes.todolist.datasource.TaskDataBase
import com.welingtongomes.todolist.model.Task

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dataBase: TaskDataBase

    private val adapter by lazy { TaskListAdapter() }
    private val register =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) updatedList()
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataBase = TaskDataBase(this)
        updatedList()
        binding.rvTask.adapter = adapter
        insertListeners()
    }


    private fun insertListeners() {
        binding.fab.setOnClickListener {
            register.launch(Intent(this, ActivityAddTask::class.java))
        }
        adapter.listenerEdit = {
            val intent = Intent(this, ActivityAddTask::class.java)
            intent.putExtra(ActivityAddTask.TASK_ID, it.id)
            register.launch(intent)
        }
        adapter.listenerDelete = {
            val deleteTask = dataBase.deleteTask(it.id)
            if (deleteTask > 0){
                Toast.makeText(this," Registro removido ", Toast.LENGTH_SHORT).show()
                updatedList()
            }
        }

    }


    private fun updatedList() {
        Thread{
            val list = dataBase.getTask() as MutableList<Task>
            runOnUiThread{
                if (list.isEmpty()) {
                    binding.includeEmpty.emptyLayout.visibility = View.VISIBLE
                } else {
                    binding.includeEmpty.emptyLayout.visibility = View.GONE
                }
                adapter.setList(list)
            }
        }.start()

    }


}
