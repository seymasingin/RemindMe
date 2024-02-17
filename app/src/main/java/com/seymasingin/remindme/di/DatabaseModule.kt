package com.seymasingin.remindme.di

import android.content.Context
import androidx.room.Room
import com.seymasingin.remindme.data.TodoDatabase
import com.seymasingin.remindme.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, TodoDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDao(db: TodoDatabase) = db.todoDao()
}