import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.api.Test
import kotlinx.serialization.json.Json
import org.savejson.services.PostSavingService
import org.savejson.model.Post
import java.io.File

class PostSaveServiceTests {

    private val posts = listOf(Post(1, 1, "Test title", "Test body"))

    @Test
    fun `Test save posts if folder don't exist`(@TempDir tempDir: File) {
        val subFolder = File(tempDir, "posts")
        PostSavingService.savePosts(posts, subFolder.path)
        assertTrue(subFolder.exists())
    }

    @Test
    fun `Test save posts if folder exist`(@TempDir tempDir: File) {
        val subFolder = File(tempDir, "posts")
        subFolder.mkdir()
        PostSavingService.savePosts(posts, subFolder.path)
        assertTrue(subFolder.exists())
    }

    @Test
    fun `Test save empty posts list`(@TempDir tempDir: File) {
        val folderPath = "${tempDir.path}/empty_posts"
        val folder = File(folderPath)
        assertFalse(folder.exists())
        PostSavingService.savePosts(emptyList(), folderPath)
        assertFalse(folder.exists())
    }

    @Test
    fun `Test saved file is correctly parsed to JSON`(@TempDir tempDir: File) {
        val folderPath = "${tempDir.path}/parsed_posts"
        PostSavingService.savePosts(posts, folderPath)

        posts.forEach { originalPost ->
            val filePath = "${folderPath}/${originalPost.id}.json"
            val savedFile = File(filePath)
            assertTrue(savedFile.exists())

            val fileContent = savedFile.readText()
            val parsedPost = Json.decodeFromString(Post.serializer(), fileContent)

            assertEquals(originalPost, parsedPost)
        }
    }
}
