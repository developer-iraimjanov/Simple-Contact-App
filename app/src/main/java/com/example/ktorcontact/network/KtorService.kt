package com.example.ktorcontact.network

import com.example.ktorcontact.models.ContactRequest
import com.example.ktorcontact.models.ContactResponse
import com.example.ktorcontact.models.Response
import com.example.ktorcontact.repository.KtorServiceImp
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*

interface KtorService {
    suspend fun getContacts(): List<ContactResponse>
    suspend fun addContacts(contactRequest: ContactRequest): Response
    suspend fun updateContacts(contactResponse: ContactResponse): Response
    suspend fun deleteContacts(id: Int): Response

    companion object {
        fun create(): KtorService {
            return KtorServiceImp(
                client = HttpClient(Android) {

                    install(HttpTimeout){
                        socketTimeoutMillis = 30000
                        requestTimeoutMillis = 30000
                        connectTimeoutMillis = 30000
                    }

                    install(Logging) {
                        level = LogLevel.ALL
                    }
                    install(ContentNegotiation) {
                        json()
                    }
                }
            )
        }
    }

}