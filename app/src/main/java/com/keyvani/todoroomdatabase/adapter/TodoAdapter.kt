package com.keyvani.todoroomdatabase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.keyvani.todoroomdatabase.databinding.ItemTodoBinding
import com.keyvani.todoroomdatabase.db.TodoDatabase
import com.keyvani.todoroomdatabase.db.TodoEntity
import com.keyvani.todoroomdatabase.repository.DbRepository
import com.keyvani.todoroomdatabase.utils.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoAdapter @Inject constructor(): RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    @Inject
    lateinit var repository: DbRepository

    @Inject
    lateinit var todoEntity: TodoEntity

    private lateinit var binding: ItemTodoBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemTodoBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TodoEntity) {
            binding.apply {
                tvItemTodo.text = "${item.todoTitle}"
                checkBox.isChecked = item.isChecked

                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    when (isChecked) {
                        true -> {
                            todoEntity = TodoEntity(item.todoId, "${item.todoTitle}", true)
                            repository.updateTodo(todoEntity)

                        }
                        false -> {
                            todoEntity = TodoEntity(item.todoId, "${item.todoTitle}", false)
                            repository.updateTodo(todoEntity)
                        }
                    }
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<TodoEntity>() {
        override fun areItemsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
            return oldItem.todoId == newItem.todoId
        }

        override fun areContentsTheSame(oldItem: TodoEntity, newItem: TodoEntity): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)


}