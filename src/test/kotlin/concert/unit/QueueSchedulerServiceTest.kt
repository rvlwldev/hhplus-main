package concert.unit

import io.hhplus.concert.ConcertApplication
import io.hhplus.concert.domain.queue.Queue
import io.hhplus.concert.domain.queue.QueueSchedulerService
import io.hhplus.concert.domain.queue.QueueStatus
import io.hhplus.concert.infrastructure.queue.QueueJpaRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@SpringBootTest(classes = [ConcertApplication::class])
@EnableScheduling
@Transactional
class QueueSchedulerServiceTest @Autowired constructor(
    val queueSchedulerService: QueueSchedulerService,
    val queueJpaRepository: QueueJpaRepository
) {

    @BeforeEach
    fun setup() {
        queueJpaRepository.deleteAll()
    }

    @Test
    fun `스케줄러가 대기열을 처리`() {
        // Given
        val scheduleId = 1L
        val queues = (1..30).map { i ->
            Queue(
                userId = i.toLong(),
                scheduleId = scheduleId,
                status = QueueStatus.WAIT,
                createdAt = ZonedDateTime.now().minusMinutes(i.toLong())
            )
        }
        queueJpaRepository.saveAll(queues)

        // When
        queueSchedulerService.queuePass() // 스케줄러 메서드 직접 호출

        // Then
        val passedQueues = queueJpaRepository.findAll()
            .filter { it.status == QueueStatus.PASS }
        assertEquals(30, passedQueues.size)
    }

    @Test
    fun `스케줄러가 시간 초과된 대기열을 삭제`() {
        // Given
        val scheduleId = 1L
        val currentQueues = (1..10).map { i ->
            Queue(
                userId = i.toLong(),
                scheduleId = scheduleId,
                status = QueueStatus.PASS,
                updatedAt = ZonedDateTime.now().minusMinutes(5)
            )
        }
        val expiredQueues = (11..20).map { i ->
            Queue(
                userId = i.toLong(),
                scheduleId = scheduleId,
                status = QueueStatus.PASS,
                updatedAt = ZonedDateTime.now().minusMinutes(15)
            )
        }
        queueJpaRepository.saveAll(currentQueues)
        queueJpaRepository.saveAll(expiredQueues)

        // When
        queueSchedulerService.deleteTimeoutQueues()

        // Then
        val resultQueues = queueJpaRepository.findAll()
        assertEquals(10, resultQueues.size)
    }
}