package service

import io.hhplus.concert.domain.user.User
import io.hhplus.concert.domain.user.UserRepository
import io.hhplus.concert.domain.user.UserService
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var userService: UserService

    @Test
    fun `save user should return saved user response`() {
        val user = User(id = 1L, name = "John Doe", point = 100)

        whenever(userRepository.save(any<User>())).thenReturn(user)
        val response = userService.save(user)

        assertEquals(user.id, response.id)
        assertEquals(user.name, response.name)
    }

    @Test
    fun `get user by id should return user response if found`() {
        val user = User(id = 1L, name = "John Doe", point = 100)
        whenever(userRepository.findById(1L)).thenReturn(user)

        val response = userService.get(1L)

        assertEquals(user.id, response.id)
        assertEquals(user.name, response.name)
    }

    @Test
    fun `get user by id should throw exception if not found`() {
        whenever(userRepository.findById(1L)).thenReturn(null)

        val exception = assertThrows<IllegalArgumentException> {
            userService.get(1L)
        }

        assertEquals("존재하지 않는 유저입니다.", exception.message)
    }

    @Test
    fun `charge point should increase user point`() {
        val user = User(id = 1L, name = "John Doe")

        whenever(userRepository.findById(1L)).thenReturn(user)
        user.chargePoint(100L)
        whenever(userRepository.save(any<User>())).thenReturn(user)

        val response = userService.chargePoint(1L, 50L)

        assertEquals(150L, response.point)
    }

    @Test
    fun `use point should decrease user point`(): Unit {
        val user = User(id = 1L, name = "John Doe")

        whenever(userRepository.findById(1L)).thenReturn(user)
        user.chargePoint(100L)
        whenever(userRepository.save(any<User>())).thenReturn(user)

        val response = userService.usePoint(1L, 50L)

        assertEquals(50L, response.point)
    }

    @Test
    fun `use point should throw exception if points are insufficient`() {
        val user = User(id = 1L, name = "John Doe", point = 30)
        whenever(userRepository.findById(1L)).thenReturn(user)

        val exception = assertThrows<IllegalArgumentException> {
            userService.usePoint(1L, 50L)
        }

        assertEquals("보유 포인트가 부족합니다.", exception.message)
    }
}