package com.keyvani.todoroomdatabase.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.keyvani.todoroomdatabase.utils.Constants

@Entity(tableName = Constants.TODO_TABLE)
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    var todoId: Int = 0,
    var todoTitle: String = "",
    var isChecked: Boolean
)
