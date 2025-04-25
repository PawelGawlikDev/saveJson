package org.savejson

import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking
import org.savejson.interfaces.JsonPlaceholderNotAvailableException
import org.savejson.services.PostSavingService
import org.savejson.services.PostService
import java.util.logging.Logger
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation


fun main() {
    val logger = Logger.getLogger("MainKtLogger")
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }
    runBlocking {
        val postService = PostService(client)
        try {
            val posts = postService.fetchPosts()
            logger.info("Fetching posts")
            PostSavingService.savePosts(posts)
        } catch (e: Exception) {
           throw JsonPlaceholderNotAvailableException("Could not fetch posts", e)
        } finally {
            postService.close()
            logger.info("Posts saved in posts folder posts")
        }

    }
}
