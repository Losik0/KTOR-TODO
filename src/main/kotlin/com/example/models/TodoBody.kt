package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class TodoBody(
    val id: Int,
    val contents: String,
    val is_finished: Boolean
)

