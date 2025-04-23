import org.junit.jupiter.api.Assertions.assertTrue
import org.savejson.file.Files
import org.savejson.model.Post
import java.io.File
import kotlin.test.*

val posts = listOf(Post(1, 1, "Test title", "Test body"))
const val tempDir = "test_posts"

class FilesTests {

    @AfterTest
    fun cleanup() {
        File(tempDir).deleteRecursively()
    }

    @Test
    fun `Test save posts if folder don't exist`() {
        Files.savePosts(posts, tempDir)
        val file = File(tempDir)
        assertTrue(file.exists())
    }

    @Test
    fun `Test save posts if folder exist`() {
        File(tempDir).mkdir()
        Files.savePosts(posts, tempDir)
        val file = File(tempDir)
        assertTrue(file.exists())
    }
}