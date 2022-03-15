package com.example.services

import arrow.core.*
import com.example.database.TodoTable
import com.example.models.TodoBody
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import com.example.plugins.ItemNotFoundException

class TodoServiceImpl(val db: Database): TodoService {

    override fun init(){
        transaction(db){
            SchemaUtils.create(TodoTable)
            val todos = listOf(TodoBody(1,"first contents", false),
                TodoBody(2, "eat buffalo wings", true),
                TodoBody(3, "Study Kotlin", false))
            TodoTable.batchInsert(todos){
                this[TodoTable.contents] = it.contents
                this[TodoTable.is_finished] = it.is_finished
            }
        }
    }

    override fun createTodo(contents: String) = transaction(db) {
        val id = TodoTable.insertAndGetId { it[TodoTable.contents]=contents }
        TodoTable.select{TodoTable.id eq id}.map{
            TodoBody(it[TodoTable.id].value, it[TodoTable.contents], it[TodoTable.is_finished])
        }.singleOrNull()
    }

    override fun updateTodo(id: Int, contents: String){
        transaction(db) {
            TodoTable.update({TodoTable.id eq id}){
                it[TodoTable.contents] = contents
            }
        }
    }

    override fun completeTodo(id: Int) {
        transaction(db) {
            TodoTable.update({TodoTable.id eq id}){
                it[is_finished] = true
            }
        }
    }

    override fun deleteTodo(id: Int){
        transaction(db) {
            TodoTable.deleteWhere { TodoTable.id eq id }
        }
    }

    override fun deleteAllCompletedTodo() {
        transaction(db){
            TodoTable.deleteWhere { TodoTable.is_finished eq true }
        }
    }

    override fun getTodo(id: Int): TodoBody = transaction(db) {
        TodoTable.select{TodoTable.id eq id}.map{
            TodoBody(it[TodoTable.id].value, it[TodoTable.contents], it[TodoTable.is_finished])
        }.firstOrNone().getOrElse {
            throw ItemNotFoundException("")
        }
    }

    override fun getAllTodo(): List<TodoBody> = transaction(db) {
        with(TodoTable) {
            selectAll().map{
                TodoBody(it[TodoTable.id].value, it[TodoTable.contents], it[TodoTable.is_finished])
            }
        }
    }/*.toEither {
        ItemNotFoundException("")
    }.getOrHandle {
        throw it
    }*/


    override fun getTodoListByOption(is_finished: Boolean): List<TodoBody> = transaction(db) {
        TodoTable.select{TodoTable.is_finished eq is_finished}.map {
            TodoBody(it[TodoTable.id].value, it[TodoTable.contents], it[TodoTable.is_finished])
        }
    }

    companion object {
        val module = DI.Module(TodoService::class.simpleName!!) {
            bind<TodoService>() with singleton {
                TodoServiceImpl(instance())
            }
        }
    }

}