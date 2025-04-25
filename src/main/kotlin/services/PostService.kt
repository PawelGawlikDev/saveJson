package org.savejson.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import org.savejson.model.Post

class PostService (private val client: HttpClient) {
    private val postsPath = "https://jsonplaceholder.typicode.com/posts"

    suspend fun fetchPosts(): List<Post> {
        val response = client.get(postsPath)
        if (!response.status.isSuccess()) {
            throw Exception("Response error ${response.status.description}")
        }
        return response.body()
    }

    fun close() {
        client.close()
    }

}