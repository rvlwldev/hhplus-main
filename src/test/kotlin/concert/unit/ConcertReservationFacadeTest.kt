package concert.unit

import io.hhplus.concert.application.ConcertReservationFacade
import io.hhplus.concert.application.TokenType
import io.hhplus.concert.application.component.TokenProvider
import io.hhplus.concert.domain.concert.ConcertService
import io.hhplus.concert.domain.queue.QueueInfo
import io.hhplus.concert.domain.queue.QueueService
import io.hhplus.concert.domain.queue.QueueStatus
import io.hhplus.concert.domain.user.service.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.ZonedDateTime

class ConcertReservationFacadeTest {
    private val tokenProvider: TokenProvider = mock(TokenProvider::class.java)
    private val userService: UserService = mock(UserService::class.java)
    private val concertService: ConcertService = mock(ConcertService::class.java)
    private val queueService: QueueService = mock(QueueService::class.java)
    private val facade = ConcertReservationFacade(tokenProvider, userService, concertService, queueService)

    @Test
    fun `대기열 큐생성과 대기열 토큰 반환`() {
        // Given
        val userId = 1L
        val scheduleId = 1L
        val createdAt = ZonedDateTime.now()
        val queueInfo = QueueInfo(1L, userId, scheduleId, QueueStatus.WAIT.name, createdAt)
        val token = "testToken"

        `when`(queueService.create(userId, scheduleId)).thenReturn(queueInfo)
        `when`(tokenProvider.createQueueToken(userId, scheduleId, createdAt)).thenReturn(token)

        // When
        val result = facade.reserve(userId, scheduleId)

        // Then
        assertEquals(TokenType.QUEUE, result.type)
        assertEquals(token, result.token)
    }

    @Test
    fun `대기열 통과 시 결제 토큰 반환`() {
        // Given
        val token = "queueToken"
        val scheduleId = 1L
        val userId = 1L
        val claims = mapOf("userId" to userId)
        val queueInfo = QueueInfo(1L, userId, scheduleId, QueueStatus.PASS.name, ZonedDateTime.now())
        val paymentToken = "paymentToken"

        `when`(tokenProvider.validateQueueToken(token)).thenReturn(claims)
        `when`(queueService.get(userId, scheduleId)).thenReturn(queueInfo)
        `when`(tokenProvider.createPaymentToken(userId, scheduleId)).thenReturn(paymentToken)

        // When
        val result = facade.waitQueue(token, scheduleId)

        // Then
        assertEquals(TokenType.PAYMENT, result.type)
        assertEquals(paymentToken, result.token)
    }

    @Test
    fun `대기열 통과 하기 전엔 대기열 토큰 반환`() {
        // Given
        val token = "queueToken"
        val scheduleId = 1L
        val userId = 1L
        val claims = mapOf("userId" to userId)
        val queueInfo = QueueInfo(1L, userId, scheduleId, QueueStatus.WAIT.name, ZonedDateTime.now())

        `when`(tokenProvider.validateQueueToken(token)).thenReturn(claims)
        `when`(queueService.get(userId, scheduleId)).thenReturn(queueInfo)

        // When
        val result = facade.waitQueue(token, scheduleId)

        // Then
        assertEquals(TokenType.QUEUE, result.type)
        assertEquals(token, result.token)
    }
}