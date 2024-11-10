package facade

import io.hhplus.concert.ConcertApplication
import io.hhplus.concert.application.reservation.ReservationFacade
import io.hhplus.concert.application.support.DistributedLocker
import io.hhplus.concert.application.support.TokenManager
import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.queue.Queue
import io.hhplus.concert.domain.queue.QueueInfo
import io.hhplus.concert.domain.queue.QueueService
import io.hhplus.concert.domain.schedule.Schedule
import io.hhplus.concert.domain.schedule.ScheduleInfo
import io.hhplus.concert.domain.schedule.ScheduleService
import io.hhplus.concert.domain.user.User
import io.hhplus.concert.domain.user.UserInfo
import io.hhplus.concert.domain.user.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(classes = [ConcertApplication::class])
class ReservationFacadeTest {

    @Autowired
    private lateinit var reservationFacade: ReservationFacade

    @MockBean
    private lateinit var distributedLocker: DistributedLocker

    @MockBean
    private lateinit var tokenManager: TokenManager

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var scheduleService: ScheduleService

    @MockBean
    private lateinit var queueService: QueueService

    private val userId: Long = 1L
    private val scheduleId: Long = 100L
    val lockKey = "reservation:lock:$scheduleId"

    @BeforeEach
    fun setup() {
        val user = UserInfo(User(id = userId, name = "Test User"))
        val schedule = ScheduleInfo(Schedule(id = scheduleId, concertId = 1L, reservableCount = 50))
        val queue = QueueInfo(Queue(id = 1L, userId = userId, scheduleId = scheduleId))

        given(userService.get(userId)).willReturn(user)
        given(scheduleService.get(scheduleId)).willReturn(schedule)
        given(queueService.create(userId, scheduleId)).willReturn(queue)
        given(tokenManager.createQueueToken(queue.id, userId))
            .willReturn("test-token")
    }

    @Test
    @Transactional
    fun `락이 없을때, 대기열 토큰 반환 성공`() {
        given(distributedLocker.tryLock(lockKey)).willReturn("lockValue")

        val result = reservationFacade.reserve(userId, scheduleId)
        assertNotNull(result.token)
    }

    @Test
    @Transactional
    fun `락 획득 실패 시 TOO_MANY_REQUEST 예외 발생`() {
        given(distributedLocker.tryLock(lockKey)).willReturn(null)

        val exception = assertThrows<BizException> {
            reservationFacade.reserve(userId, scheduleId)
        }

        assertEquals(BizError.Schedule.TOO_MANY_REQUEST.second, exception.message)
    }
}