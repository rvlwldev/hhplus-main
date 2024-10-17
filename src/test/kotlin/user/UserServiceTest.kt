package user

import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.user.UserRepository
import io.hhplus.concert.domain.user.entity.PointHistoryType
import io.hhplus.concert.domain.user.entity.User
import io.hhplus.concert.domain.user.entity.UserPointHistory
import io.hhplus.concert.domain.user.service.UserService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.http.HttpStatus

class UserServiceTest {

    private val repo: UserRepository = mock()
    private val service = UserService(repo)

    @Test
    fun `포인트 조회 시 유저가 존재하지 않을 때`() {
        // Given
        val userId = 1L

        whenever(repo.findById(userId)).thenReturn(null)

        // When & Then
        val exception = assertThrows(BizException::class.java) {
            service.getPoint(userId)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.code)
        assertEquals("유저를 찾을 수 없습니다.", exception.message)
    }

    @Test
    fun `포인트 충전 시 유저가 존재하지 않을 때`() {
        // Given
        val userId = 1L
        val amount = 1000L

        whenever(repo.findById(userId)).thenReturn(null)

        // When & Then
        val exception = assertThrows(BizException::class.java) {
            service.chargePoint(userId, amount)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.code)
        assertEquals("유저를 찾을 수 없습니다.", exception.message)
    }

    @Test
    fun `포인트 충전 시 유저의 포인트와 이력 저장`() {
        // Given
        val userId = 1L
        val amount = 1000L
        val user = User(userId = userId, point = 0L)

        whenever(repo.findById(userId)).thenReturn(user)
        whenever(repo.save(any())).thenReturn(user)
        whenever(repo.saveHistory(any())).thenReturn(
            UserPointHistory(
                user = user,
                type = PointHistoryType.CHARGE
            )
        )

        // When
        val result = service.chargePoint(userId, amount)

        // Then
        assertEquals(amount, result.point)
        verify(repo).save(user)
        verify(repo).saveHistory(any())
    }
}