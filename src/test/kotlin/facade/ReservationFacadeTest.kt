package facade

import io.hhplus.concert.application.reservation.ReservationFacade
import io.hhplus.concert.application.support.TokenManager
import io.hhplus.concert.core.exception.BizError
import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.concert.ConcertService
import io.hhplus.concert.domain.payment.PaymentService
import io.hhplus.concert.domain.queue.Queue
import io.hhplus.concert.domain.queue.QueueInfo
import io.hhplus.concert.domain.queue.QueueService
import io.hhplus.concert.domain.queue.QueueStatus
import io.hhplus.concert.domain.schedule.Schedule
import io.hhplus.concert.domain.schedule.ScheduleInfo
import io.hhplus.concert.domain.schedule.ScheduleService
import io.hhplus.concert.domain.user.User
import io.hhplus.concert.domain.user.UserInfo
import io.hhplus.concert.domain.user.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class ReservationFacadeTest {

    @Mock
    private lateinit var tokenManager: TokenManager

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var concertService: ConcertService

    @Mock
    private lateinit var scheduleService: ScheduleService

    @Mock
    private lateinit var queueService: QueueService

    @Mock
    private lateinit var paymentService: PaymentService

    @InjectMocks
    private lateinit var sut: ReservationFacade

    val token = "token"

    @BeforeEach
    fun setUp() {
        whenever(userService.get(1L)).thenReturn(UserInfo(User(1)))

        val schedule = Schedule(1, 1L, reservableCount = 50)
        whenever(scheduleService.get(1L)).thenReturn(ScheduleInfo(schedule))

        val queue = Queue(id = 1L, userId = 1L, scheduleId = 1L)
        whenever(queueService.create(1L, 1L)).thenReturn(QueueInfo(queue))

        val token = "token"
        whenever(tokenManager.createQueueToken(queue.id, 1L, 1L, QueueStatus.WAIT.name))
            .thenReturn(token)
    }

    @Test
    fun `대기열을 생성할 때, 같은 아이디로 중복 생성하면 DUPLICATED 커스텀 예외 발생`() {
        // given
        val userId = 1L
        val scheduleId = 1L

        // when
        sut.reserve(userId, scheduleId)
        `when`(queueService.create(userId, scheduleId))
            .thenThrow(BizException(BizError.Queue.DUPLICATED))

        // then
        assertThrows<BizException> {
            sut.reserve(userId, scheduleId)
        }
    }

    @Test
    fun `좌석 결제 성공`() {
        // given
        val userId = 1L
        val scheduleId = 1L

        // when
        val result = sut.reserve(userId, scheduleId)

        // then
        assertEquals(token, result.token)
        assertEquals(1L, result.scheduleId)
        assertEquals(token, result.token)
    }
}

