package com.example.ktorcontact.models

import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val error: Boolean = false,
    val message: String = ""
)