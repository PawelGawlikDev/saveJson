package org.savejson.services


import java.io.File
import kotlinx.serialization.json.Json
import org.savejson.model.Post
import java.util.logging.Logger
import java.util.logging.Level


object PostSavingService {
    private val logger = Logger.getLogger(javaClass.name)

    fun savePosts(posts: List<Post>, folderPath: String = "posts") {
        if (posts.isEmpty()) {
            logger.log(Level.WARNING, "No posts to save")
            return
        }
        val folder = File(folderPath)
        if (!folder.exists()) {
            folder.mkdir()
            logger.log(Level.INFO, "Create folder $folder")
        }

        posts.forEach {
            val file = File("${folderPath}/${it.id}.json")
            file.writeText(Json.encodeToString(Post.serializer(), it))
        }
    }
}