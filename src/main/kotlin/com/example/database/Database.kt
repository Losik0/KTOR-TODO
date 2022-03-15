package com.example.database

import com.typesafe.config.ConfigFactory
import org.jetbrains.exposed.sql.Database
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val dataSource = DI.Module("Database"){
    bind<Database>() with singleton {
        connectDatabase()
    }
}

fun connectDatabase(): Database {
    val dbConfig = ConfigFactory.load("application.conf").getConfig("database")
    val url = dbConfig.getString("local.url")
    val driver = dbConfig.getString("local.driver")
    return Database.connect(url=url, driver=driver)
}

