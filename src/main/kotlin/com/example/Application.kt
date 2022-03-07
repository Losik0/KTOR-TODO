package com.example

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import com.example.routes.registerTodoRoutes
import com.example.services.TodoService
import com.example.services.TodoServiceImpl
import com.typesafe.config.ConfigFactory
import io.ktor.request.*
import org.jetbrains.exposed.sql.Database
import org.slf4j.event.Level

/*
fun main() {
    embeddedServer(Netty, port = 8000, host = "localhost") {
        configureRouting()
        configureSerialization()
        registerTodoRoutes()
    }.start(wait = true)
}
 */

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    val config = ConfigFactory.load("application.conf")
    val dbUrl = config.getString("database.local.url")
    val dbDriver = config.getString("database.local.driver")
    val db = Database.connect(url=dbUrl, driver=dbDriver)

    install(ContentNegotiation){ json()}

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    val todoService: TodoService = TodoServiceImpl(db)
    todoService.init()
    registerTodoRoutes(todoService)
}
