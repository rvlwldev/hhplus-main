import io.hhplus.concert.domain.schedule.Schedule
import io.hhplus.concert.domain.seat.Seat
import io.hhplus.concert.domain.user.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ScheduleTest {

    @Test
    fun `존재하지 않는 좌석을 예약하려고 할 때`() {
        val schedule = Schedule()

        val exception = assertThrows<IllegalArgumentException> {
            schedule.isReservable(999L)
        }

        assertEquals("존재 하지 않는 좌석입니다.", exception.message)
    }

    @Test
    fun `대기열에 없는 유저의 순위를 확인하려고 할 때`() {
        val schedule = Schedule()
        val user = User(id = 1L, name = "Test User")

        val exception = assertThrows<IllegalArgumentException> {
            schedule.getUserRank(user.id)
        }

        assertEquals("해당 유저는 대기열에 없습니다.", exception.message)
    }

    @Test
    fun `예약 가능한 좌석이 없을 때 예약 가능 여부 확인`() {
        val seat1 = Seat(seatNumber = 1)
        val seat2 = Seat(seatNumber = 2)

        val user1 = User(id = 1L, name = "TEST1")
        val user2 = User(id = 2L, name = "TEST2")

        seat1.reserve(user1)
        seat1.confirm(user1)

        seat2.reserve(user2)
        seat2.confirm(user2)

        val schedule = Schedule(seats = listOf(seat1, seat2))

        assertFalse(schedule.isReservable())
    }

    @Test
    fun `예약 가능한 좌석이 있을 때 예약 가능 여부 확인`() {
        val seat1 = Seat(seatNumber = 1)
        val seat2 = Seat(seatNumber = 2)

        val user1 = User(id = 1L, name = "TEST1")
        seat1.reserve(user1)

        val schedule = Schedule(seats = listOf(seat1, seat2))

        assertTrue(schedule.isReservable())
    }
}
