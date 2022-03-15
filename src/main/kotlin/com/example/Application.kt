package com.example

import com.example.database.dataSource
import com.example.plugins.ItemNotFoundException
import io.ktor.application.*
import io.ktor.serialization.*
import com.example.routes.registerTodoRoutes
import com.example.services.todoServices
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.server.netty.*
import org.kodein.di.bind
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = EngineMain.main(args)

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

    install(StatusPages){
        exception<ItemNotFoundException> {
            call.respond(HttpStatusCode.NotFound, "존재하지 않는 항목 입니다.")
        }
        
        exception<Throwable> {
            call.respond(HttpStatusCode.InternalServerError, "오류 발생...")
        }

    }
    registerTodoRoutes()
}
