package org.savejson.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.savejson.model.Post

class PostService {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    suspend fun fetchPosts(): List<Post> {
        val response = client.get("https://jsonplaceholder.typicode.com/posts")
        if (!response.status.isSuccess()) {
            throw Exception("Response error ${response.status.description}")
        }
        return response.body()
    }

    fun close() {
        client.close()
    }

}