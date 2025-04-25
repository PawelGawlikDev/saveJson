import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test
import org.savejson.services.PostService
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.client.engine.mock.respond
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import java.nio.file.Files
import io.ktor.serialization.kotlinx.json.json
import org.savejson.model.Post
import java.nio.file.Paths
import kotlin.test.assertFailsWith

class PostServiceTests {

    val jsonContent: String = Files.readString(Paths.get("src", "test", "resources", "postsPlaceholder.json"))

    val mockEngine = MockEngine {
        respond(
            content = jsonContent,
            status = HttpStatusCode.OK,
            headers = headersOf("Content-Type", listOf(ContentType.Application.Json.toString()))
        )
    }

    val mockHttpClient = HttpClient(mockEngine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    @Test
    fun `Test fetching posts`() {
        val service = PostService(mockHttpClient)
        runBlocking {
            val posts = service.fetchPosts()
            assertTrue(posts.isNotEmpty())
        }
    }

    @Test
    fun `Test fetching correct number of posts`() {
        val service = PostService(mockHttpClient)
        runBlocking {
            val expectedPosts = Json.decodeFromString<List<Post>>(jsonContent)
            val posts = service.fetchPosts()
            assertEquals(expectedPosts.size, posts.size)
        }
    }

    @Test
    fun `Test fetching posts with correct data`() {
        val service = PostService(mockHttpClient)
        runBlocking {
            val expectedPosts = Json.decodeFromString<List<Post>>(jsonContent)
            val posts = service.fetchPosts()
            assertEquals(expectedPosts[0].id, posts[0].id)
            assertEquals(expectedPosts[0].title, posts[0].title)
            assertEquals(expectedPosts[0].body, posts[0].body)
        }
    }

    @Test
    fun `Test fetching posts handles error response`() {
        val errorJsonContent = """{"message": "Wystąpił błąd"}"""
        val errorMockEngine = MockEngine {
            respond(
                content = errorJsonContent,
                status = HttpStatusCode.InternalServerError,
                headers = headersOf("Content-Type", listOf(ContentType.Application.Json.toString()))
            )
        }
        val errorMockHttpClient = HttpClient(errorMockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
        }
        val serviceWithError = PostService(errorMockHttpClient)

        runBlocking {
            assertFailsWith<Exception> {
                serviceWithError.fetchPosts()
            }.also {
                assertTrue(it.message!!.contains("Response error ${HttpStatusCode.InternalServerError.description}"))
            }
        }
    }
}