package com.welingtongomes.todolist.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.welingtongomes.todolist.databinding.ActivityMainBinding
import com.welingtongomes.todolist.datasource.TaskDataSource
import com.welingtongomes.todolist.model.Task

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val adapter by lazy { TaskListAdapter() }
    private val register =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) updatedList()
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
            TaskDataSource.remover(it)
            updatedList()
        }
    }



    fun updatedList() {
        val list = TaskDataSource.getList()
        if (list.isEmpty()){
            binding.includeEmpty.emptyLayout.visibility = View.VISIBLE
        }else{
            binding.includeEmpty.emptyLayout.visibility = View.GONE
        }
        adapter.setList(TaskDataSource.getList())
    }


}