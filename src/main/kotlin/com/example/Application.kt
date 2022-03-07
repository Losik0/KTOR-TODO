package com.example

import com.example.database.dataSource
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import com.example.routes.registerTodoRoutes
import com.example.services.TodoServiceImpl
import com.example.services.todoServices
import com.typesafe.config.ConfigFactory
import io.ktor.request.*
import org.jetbrains.exposed.sql.Database
import org.kodein.di.bind
import org.kodein.di.ktor.di
import org.kodein.di.singleton
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
    di {
        bind {
            singleton {
                environment.config
            }
        }

        importOnce(dataSource)
        importOnce(todoServices)
    }

    install(ContentNegotiation){ json()}

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    registerTodoRoutes()
}
