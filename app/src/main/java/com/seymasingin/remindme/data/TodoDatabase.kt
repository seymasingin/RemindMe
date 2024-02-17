package com.seymasingin.remindme.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seymasingin.remindme.data.models.ToDoTask

@Database(entities = [ToDoTask::class], version = 1, exportSchema = false)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao
}