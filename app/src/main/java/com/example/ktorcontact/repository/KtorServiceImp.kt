package com.example.ktorcontact.repository

import com.example.ktorcontact.models.ContactRequest
import com.example.ktorcontact.models.ContactResponse
import com.example.ktorcontact.models.Response
import com.example.ktorcontact.network.KtorService
import com.example.ktorcontact.network.Routes
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.util.*

class KtorServiceImp(
    private val client: HttpClient
) : KtorService {

    override suspend fun getContacts(): List<ContactResponse> {
        return client.get { url(Routes.GET) }.body()
    }

    override suspend fun addContacts(contactRequest: ContactRequest): Response {
        return client.post {
            url(Routes.POST)
            contentType(ContentType.Application.Json)
            setBody(contactRequest)
        }.body()
    }

    override suspend fun updateContacts(contactResponse: ContactResponse): Response {
        return client.put {
            url(Routes.PUT)
            contentType(ContentType.Application.Json)
            setBody(contactResponse)
        }.body()
    }

    @OptIn(InternalAPI::class)
    override suspend fun deleteContacts(id: Int): Response {
        return client.delete {
            url(Routes.DELETE)
            body = FormDataContent(Parameters.build {
                append("id", "$id")
            })
        }.body()
    }

}
