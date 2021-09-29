package com.welingtongomes.todolist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.welingtongomes.todolist.databinding.ActivityAddTaskBinding
import com.welingtongomes.todolist.datasource.TaskDataSource
import com.welingtongomes.todolist.extensions.format
import com.welingtongomes.todolist.extensions.text
import com.welingtongomes.todolist.model.Task
import java.util.*

class Activity_add_task : AppCompatActivity() {
    lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            val task = Task(
                    title = binding.inputTitle.text,
                    data = binding.inputData.text,
                    hora = binding.inputHora.text,
            )
            TaskDataSource.insertTask(task)
            Log.i("TESTE TASK","LIst TASK:\n" + TaskDataSource.getList())
        }
    }
}