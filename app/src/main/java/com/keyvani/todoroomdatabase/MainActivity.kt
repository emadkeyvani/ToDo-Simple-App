package com.keyvani.todoroomdatabase

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.keyvani.todoroomdatabase.adapter.TodoAdapter
import com.keyvani.todoroomdatabase.databinding.ActivityMainBinding
import com.keyvani.todoroomdatabase.db.TodoEntity
import com.keyvani.todoroomdatabase.repository.DbRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var repository: DbRepository

    @Inject
    lateinit var todoEntity: TodoEntity

    @Inject
    lateinit var todoAdapter: TodoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.apply {
            btnAdd.setOnClickListener {
                val todo = etTodo.text.toString()
                if (todo.isNotEmpty()) {
                    todoEntity.todoId = 0
                    todoEntity.todoTitle = etTodo.text.toString()
                    todoEntity.isChecked = false
                    repository.insertTodo(todoEntity)
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
                    repository.deleteTodo(todoToDelete)
                    setupRecyclerView()
                    Snackbar.make(binding.root, "Item Deleted!", Snackbar.LENGTH_LONG).apply {
                        setAction("UNDO") {
                            repository.insertTodo(todoToDelete)
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
            if (repository.getAllTodos().isNotEmpty()) {
                RvTodo.visibility = View.VISIBLE
                tvEmptyList.visibility = View.GONE
            } else {
                RvTodo.visibility = View.GONE
                tvEmptyList.visibility = View.VISIBLE
            }
            todoAdapter.differ.submitList(repository.getAllTodos())
            RvTodo.apply {
                todoAdapter.differ.submitList(repository.getAllTodos())
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = todoAdapter
            }
        }
    }

}