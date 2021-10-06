package com.welingtongomes.todolist.datasource

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.welingtongomes.todolist.model.Task


class TaskDataBase(context: Context) : SQLiteOpenHelper (
        context, DATA_BASE_NAME, null, DATA_BASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE)
        onCreate(db)
    }

    fun insertTask(task: Task): Int {
        val db = this.writableDatabase
        var id = 0
        try {
            val values = ContentValues()
            values.put(COLUMNS_TITLE, task.title)
            values.put(COMLUMNS_DATE, task.date)
            values.put(COMLUMNS_HOUR, task.hour)

            id =
                    db.insertOrThrow(TABLE_NAME, null,values).toInt()

        }catch (e : Exception){
            e.printStackTrace()
        }
        return id
    }

    fun getTask() : List<Task>{
        val list = ArrayList<Task>()
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        try {
            if (cursor.moveToFirst()){
                do {
                    val title = cursor.getString(cursor.getColumnIndex(COLUMNS_TITLE))
                    val date = cursor.getString(cursor.getColumnIndex(COMLUMNS_DATE))
                    val hour = cursor.getString(cursor.getColumnIndex(COMLUMNS_HOUR))
                    val id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
                    val task = Task(
                            title,
                            date,
                            hour,
                            id
                    )
                    list.add(task)
                }while (cursor.moveToNext())
            }

        }catch (e : Exception){
            e.printStackTrace()
        }finally {
            if (!cursor.isClosed){
                cursor.close()
            }
        }
        return list
    }

    fun deleteTask(id : Int) : Int{
        val db : SQLiteDatabase = writableDatabase
        var row = 0
        try {
            row = db.delete(TABLE_NAME,"${BaseColumns._ID} = ?", arrayOf(id.toString()))
        }catch (e : Exception) {
            e.printStackTrace()
        }
        return row
    }


    fun updatedTask(task: Task): Int {
        val db : SQLiteDatabase = writableDatabase
        var idRow = 0
        val values = ContentValues()
        values.put(COLUMNS_TITLE, task.title)
        values.put(COMLUMNS_DATE, task.date)
        values.put(COMLUMNS_HOUR, task.hour)
        try {
           idRow = db.update(TABLE_NAME, values, "${BaseColumns._ID} = ?", arrayOf(task.id.toString()))
        }catch (e : Exception){
            e.printStackTrace()
        }
        return idRow
    }

    companion object{
        const val DATA_BASE_NAME = "task_database"
        const val DATA_BASE_VERSION = 1

        const val TABLE_NAME = "task"
        const val COLUMNS_TITLE = "title"
        const val COMLUMNS_DATE = "date"
        const val COMLUMNS_HOUR = "hour"

        const val SQL_CREATE =
                "CREATE TABLE $TABLE_NAME (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, "+
                "$COLUMNS_TITLE TEXT, "+
                "$COMLUMNS_DATE TEXT, "+
                "$COMLUMNS_HOUR TEXT)"

        const val SQL_DELETE =
                "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}