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
}
