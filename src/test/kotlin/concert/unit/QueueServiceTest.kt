package concert.unit

import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.queue.Queue
import io.hhplus.concert.domain.queue.QueueRepository
import io.hhplus.concert.domain.queue.QueueService
import io.hhplus.concert.domain.queue.QueueStatus
import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus
import kotlin.test.Test

class QueueServiceTest {
    private val repo: QueueRepository = mock(QueueRepository::class.java)
    private val service = QueueService(repo)

    @Test
    fun `대기열 중복 실패 테스트`() {
        // Given
        val userId = 1L
        val scheduleId = 1L
        val existingQueue = Queue(userId = userId, scheduleId = scheduleId)

        `when`(repo.findByUserIdAndScheduleId(userId, scheduleId)).thenReturn(existingQueue)

        // When & Then
        val exception = assertThrows(BizException::class.java) {
            service.create(userId, scheduleId)
        }

        assertEquals(HttpStatus.CONFLICT, exception.code)
        assertEquals("이미 대기열에 등록된 유저입니다.", exception.message)
    }

    @Test
    fun `대기열 저장 테스트`() {
        // Given
        val userId = 1L
        val scheduleId = 1L
        val queue = Queue(userId = userId, scheduleId = scheduleId)

        `when`(repo.findByUserIdAndScheduleId(userId, scheduleId)).thenReturn(null)
        `when`(repo.save(any())).thenReturn(queue)

        // When
        val result = service.create(userId, scheduleId)

        // Then
        assertEquals(userId, result.userId)
        assertEquals(scheduleId, result.scheduleId)
        assertEquals(QueueStatus.WAIT.name, result.status)
    }

    @Test
    fun `대기열 미등록 테스트`() {
        // Given
        val userId = 1L
        val scheduleId = 1L

        `when`(repo.findByUserIdAndScheduleId(userId, scheduleId)).thenReturn(null)

        // When & Then
        val exception = assertThrows(BizException::class.java) {
            service.get(userId, scheduleId)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.code)
        assertEquals("등록되지 않은 대기열 입니다.", exception.message)
    }
}