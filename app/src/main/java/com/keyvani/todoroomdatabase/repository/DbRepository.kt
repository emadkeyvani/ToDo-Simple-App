package com.keyvani.todoroomdatabase.repository

import androidx.room.Dao
import com.keyvani.todoroomdatabase.db.TodoDao
import com.keyvani.todoroomdatabase.db.TodoEntity
import javax.inject.Inject

class DbRepository @Inject constructor(private val dao: TodoDao){
    fun insertTodo(entity: TodoEntity) = dao.insertTodo(entity)
    fun updateTodo(entity: TodoEntity) = dao.updateTodo(entity)
    fun deleteTodo(entity: TodoEntity) = dao.deleteTodo(entity)
    fun getTodo(id:Int) = dao.getTodo(id)
    fun getAllTodos()= dao.getAllTodos()
    fun getDeleteAllTodos() = dao.getDeleteAllTodos()





}