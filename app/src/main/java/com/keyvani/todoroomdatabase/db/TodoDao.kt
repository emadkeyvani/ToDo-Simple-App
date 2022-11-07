package com.keyvani.todoroomdatabase.db

import androidx.room.*
import com.keyvani.todoroomdatabase.utils.Constants

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodo(todo: TodoEntity)

    @Update
    fun updateTodo(todo: TodoEntity)

    @Delete
    fun deleteTodo(todo: TodoEntity)

    @Query("SELECT * FROM ${Constants.TODO_TABLE} ORDER BY todoId DESC")
    fun getAllTodos(): MutableList<TodoEntity>

    @Query("SELECT * FROM ${Constants.TODO_TABLE} WHERE todoId LIKE :id")
    fun getTodo(id: Int): TodoEntity

    @Query("DELETE FROM ${Constants.TODO_TABLE}")
    fun getDeleteAllTodos()


}