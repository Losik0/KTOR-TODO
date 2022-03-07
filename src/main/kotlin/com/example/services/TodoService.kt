package com.example.services

import com.example.models.TodoBody

interface TodoService{

    fun init()

    fun createTodo(contents: String): TodoBody?

    fun updateTodo(id:Int, contents: String)

    fun completeTodo(id: Int)

    fun deleteTodo(id:Int)

    fun deleteAllCompletedTodo()

    fun getTodo(id:Int): TodoBody?

    fun getAllTodo(): List<TodoBody>

    fun getTodoListByOption(is_finished: Boolean): List<TodoBody>
}