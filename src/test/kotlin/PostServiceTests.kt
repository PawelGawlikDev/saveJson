import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test
import org.savejson.services.PostService


class PostServiceTests {
    @Test
    fun `Test fetching posts`() {
        val service = PostService()

        runBlocking {
            val posts = service.fetchPosts()
            assertTrue(posts.isNotEmpty())
        }

    }
}