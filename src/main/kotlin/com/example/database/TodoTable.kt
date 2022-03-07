package com.example.database

import org.jetbrains.exposed.dao.id.IntIdTable

object TodoTable: IntIdTable() {
    val contents = varchar("contents", 255)
    val is_finished = bool("is_finished").default(false)
}