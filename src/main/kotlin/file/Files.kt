package org.savejson.file

import java.io.File
import kotlinx.serialization.json.Json
import org.savejson.model.Post


object Files {
    fun savePosts(posts: List<Post>, folderPath: String = "posts") {
        val folder = File(folderPath)
        if (!folder.exists()) {
            folder.mkdir()
        }
        posts.forEach {
            val file = File("${folderPath}/${it.id}.json")
            file.writeText(Json.encodeToString(Post.serializer(), it))
        }
    }
}