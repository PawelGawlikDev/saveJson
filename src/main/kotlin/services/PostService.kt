package org.savejson.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking
import org.savejson.interfaces.JsonPlaceholderNotAvailableException
import org.savejson.model.Post
import java.util.logging.Level
import java.util.logging.Logger

class PostService (private val client: HttpClient) {
    private val logger = Logger.getLogger(javaClass.name)

    private val postsPath = "https://jsonplaceholder.typicode.com/posts"

    suspend fun fetchPosts(): List<Post> {
        val response = client.get(postsPath)
        if (!response.status.isSuccess()) {
            throw JsonPlaceholderNotAvailableException("Could not fetch posts")
        }
        return response.body()
    }

    fun close() {
        client.close()
    }

    fun index() {
        runBlocking {
            try {
                val posts = fetchPosts()
                logger.log(Level.INFO,"Fetching posts")
                PostSavingService.savePosts(posts)
            } catch (e: Exception) {
                logger.log(Level.SEVERE, "Could not fetch posts", e)
            } finally {
                close()
                logger.log(Level.INFO,"Posts saved in posts folder posts")
            }
        }
    }

}