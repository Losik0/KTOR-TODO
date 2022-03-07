package com.example.services

import org.kodein.di.DI

val todoServices = DI.Module("TodoServices"){
    importOnce(TodoServiceImpl.module)
}