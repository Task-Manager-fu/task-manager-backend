import com.example.auth.AuthService
import com.example.auth.JWTConfig
import com.example.config.AppConfig
import com.example.models.RegisterRequest
import com.example.plugins.configureRouting
import com.example.user.UserRepository
import com.example.user.UserService
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.server.testing.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ApiTest {
    @Test
    fun testLogin() = testApplication {
        val userRepository = UserRepository()
        application {
            val appConfig = AppConfig.from(environment.config)
            val userService = UserService(userRepository)

            val jwtConfig = JWTConfig(appConfig.jwt)

            val authService = AuthService(userService, jwtConfig)

            configureRouting(authService , userService)
        }
        val getUserMe = client.get("/users/private/me") {

        }
        println("Response body = ${getUserMe.bodyAsText()}")

//        assertEquals(HttpStatusCode.OK, response.status)
    }
}
