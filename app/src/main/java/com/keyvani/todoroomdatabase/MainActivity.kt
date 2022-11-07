package com.keyvani.todoroomdatabase

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.keyvani.todoroomdatabase.adapter.TodoAdapter
import com.keyvani.todoroomdatabase.databinding.ActivityMainBinding
import com.keyvani.todoroomdatabase.db.TodoDatabase
import com.keyvani.todoroomdatabase.db.TodoEntity
import com.keyvani.todoroomdatabase.utils.Constants
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val todoDb: TodoDatabase by lazy {
        Room.databaseBuilder(this, TodoDatabase::class.java, Constants.TODO_DATABASE)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    private lateinit var todoEntity: TodoEntity


    private val todoAdapter: TodoAdapter by lazy { TodoAdapter() }
    private var todoID = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.apply {
            btnAdd.setOnClickListener {
                val todo = etTodo.text.toString()
                if (todo.isNotEmpty()) {
                    todoEntity = TodoEntity(0, todo, false)
                    todoDb.dao().insertTodo(todoEntity)
                    etTodo.setText("")
                    setupRecyclerView()
                } else {
                    Snackbar.make(it, "Title can not be empty", Snackbar.LENGTH_SHORT).show()
                }
            }
            val swipeToDeleteCallback = object : SwipeToDeleteCallback(this@MainActivity) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val pos = viewHolder.adapterPosition
                    val todoToDelete = todoAdapter.differ.currentList[pos]
                    todoDb.dao().deleteTodo(todoToDelete)
                    setupRecyclerView()
                    Snackbar.make(binding.root, "Item Deleted!", Snackbar.LENGTH_LONG).apply{
                        setAction("UNDO"){
                            todoDb.dao().insertTodo(todoToDelete)
                            setupRecyclerView()
                        }
                    }.show()

                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(RvTodo)


        }

    }


    private fun setupRecyclerView() {
        binding.apply {
            if (todoDb.dao().getAllTodos().isNotEmpty()) {
                RvTodo.visibility = View.VISIBLE
                tvEmptyList.visibility = View.GONE
            } else {
                RvTodo.visibility = View.GONE
                tvEmptyList.visibility = View.VISIBLE
            }
            todoAdapter.differ.submitList(todoDb.dao().getAllTodos())
            RvTodo.apply {
                todoAdapter.differ.submitList(todoDb.dao().getAllTodos())
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = todoAdapter
            }
        }
    }

}