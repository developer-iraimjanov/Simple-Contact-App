package com.example.ktorcontact.models

import kotlinx.serialization.Serializable

@Serializable
data class ContactResponse(
    var id: Int = 0,
    var name: String = "",
    var surname: String = "",
    var number: String = "",
)

@Serializable
data class ContactRequest(
    var name: String = "",
    var surname: String = "",
    var number: String = "",
)