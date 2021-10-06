package com.welingtongomes.todolist.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.welingtongomes.todolist.databinding.ActivityAddTaskBinding
import com.welingtongomes.todolist.datasource.TaskDataBase
import com.welingtongomes.todolist.extensions.format
import com.welingtongomes.todolist.extensions.text
import com.welingtongomes.todolist.model.Task
import java.util.*

class ActivityAddTask : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var dataBase : TaskDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataBase = TaskDataBase(this)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        if (intent.hasExtra(TASK_ID)){
            val taskIdExtra = intent.getIntExtra(TASK_ID, 0)
            Thread {
                val taskList : List<Task> = dataBase.getTask()
                runOnUiThread{
                    val task = taskList.find { taskIdExtra == it.id}
                    if (task != null) {
                        binding.inputTitle.text = task.title
                        binding.inputData.text = task.date
                        binding.inputHora.text = task.hour
                    }
                }
            }.start()
        }

        insertListeners()
    }

    private fun insertListeners() {
        binding.inputData.editText?.setOnClickListener {
           val dataPicker = MaterialDatePicker.Builder.datePicker().build()
            dataPicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time)*-1
                binding.inputData.text = Date(it + offset).format()
            }
           dataPicker.show(supportFragmentManager,"DataPicker")
        }

        binding.inputHora.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            timePicker.addOnPositiveButtonClickListener {
                val minute = if(timePicker.minute in 0..9) "0${timePicker.minute}" else "${timePicker.minute}"
                val hora = if (timePicker.hour in 0..9) "0${timePicker.hour}" else "${timePicker.hour}"

                binding.inputHora.text = "$hora:$minute"
            }
            timePicker.show(supportFragmentManager, "TimerPicker")
        }

        binding.cancelarButton.setOnClickListener {
            finish()
        }

        binding.criarButton.setOnClickListener {
            when {
                binding.inputTitle.text.isEmpty() -> {
                    binding.inputTitle.error = "Insira um titulo"
                    binding.inputTitle.requestFocus()
                }
                binding.inputData.text.isEmpty() ->{
                    binding.inputData.text = "Insira uma data"
                    binding.inputData.requestFocus()

                }
                binding.inputHora.text.isEmpty() -> {
                    binding.inputHora.error = "Insira um horario"
                    binding.inputHora.requestFocus() }

                else -> {
                    val task = Task(
                            title = binding.inputTitle.text,
                            date = binding.inputData.text,
                            hour = binding.inputHora.text,
                            id = intent.getIntExtra(TASK_ID, 0)
                    )
                    if (task.id != 0){
                        Thread{
                            val id: Int = dataBase.updatedTask(task)
                            runOnUiThread{
                                if (id != 0){
                                    Toast.makeText(this, " registro atualizado ", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }.start()
                    }else{
                        Thread {
                            val id = dataBase.insertTask(task)
                            runOnUiThread {
                                if (id > 0) {
                                    Toast.makeText(this, " registro salvo ", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }.start()
                    }
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    companion object{
        const val TASK_ID = "task_id"
    }
}