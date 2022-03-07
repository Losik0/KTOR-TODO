package com.example.routes

import com.example.services.TodoService
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*

fun Route.todoRouting(service: TodoService) {
    route("/todo") {
        get {
            call.respond(service.getAllTodo())
        }
        get("{id}") {
            val id = call.parameters["id"]?: return@get call.respondText(
                "Missing id", status=HttpStatusCode.BadRequest
            )
            val todo = service.getTodo(id.toInt())?: return@get call.respondText(
                    "No todo item with id $id", status=HttpStatusCode.NotFound
            )
            call.respond(todo)
        }
        post {
            val parameters = call.receiveParameters()
            val todo = service.createTodo(parameters["contents"].toString()) ?: return@post call.respondText(
                "Todo Insert Failed", status=HttpStatusCode.NotFound
            )
            call.respond(todo)
        }
        delete("{id}") {
            val id = call.parameters["id"]?: return@delete call.respond(HttpStatusCode.BadRequest)
            service.deleteTodo(id.toInt())
            call.respondText("Todo Deleted", status=HttpStatusCode.Accepted)
        }
    }
}

fun Route.todoCompletedRouting(service: TodoService){
    route("/completed") {
        put("{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            service.completeTodo(id.toInt())
            call.respondText("Todo finished", status = HttpStatusCode.Accepted)
        }
        get {
            call.respond(service.getTodoListByOption(true))
        }
        delete {
            service.deleteAllCompletedTodo()
            call.respondText("Completed Todo Deleted", status = HttpStatusCode.Accepted)
        }
    }
    route("/active"){
        get {
            call.respond(service.getTodoListByOption(false))
        }
    }
}

fun Application.registerTodoRoutes(services: TodoService){
    routing{
        todoRouting(services)
        todoCompletedRouting(services)
    }
}
