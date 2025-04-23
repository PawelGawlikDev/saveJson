package org.savejson

import kotlinx.coroutines.runBlocking
import org.savejson.file.Files
import org.savejson.services.PostService


fun main() {
    runBlocking {
        val postService = PostService()
        try {
            val posts = postService.fetchPosts()
            Files.savePosts(posts)
        } catch (e: Exception) {
            println(e)
        } finally {
            postService.close()
            println("Posts saved in posts folder")
        }

    }
}
