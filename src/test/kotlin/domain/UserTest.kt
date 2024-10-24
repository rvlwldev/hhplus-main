import io.hhplus.concert.domain.user.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserTest {

    @Test
    fun `포인트 사용 시, 1보다 작은 포인트 사용 불가`() {
        val user = User(name = "TEST", point = 100)

        val exception = assertThrows<IllegalArgumentException> {
            user.usePoint(0)  // 0 포인트 사용 시도
        }

        assertEquals("1보다 작은 포인트는 사용할 수 없습니다.", exception.message)
    }

    @Test
    fun `포인트 사용 시, 보유 포인트 부족`() {
        val user = User(name = "TEST", point = 50)

        val exception = assertThrows<IllegalArgumentException> {
            user.usePoint(100)  // 100 포인트 사용 시도 (보유한 포인트보다 많음)
        }

        assertEquals("보유 포인트가 부족합니다.", exception.message)
    }

    @Test
    fun `포인트 충전 시, 1보다 작은 포인트 충전 불가`() {
        val user = User(name = "TEST", point = 100)

        val exception = assertThrows<IllegalArgumentException> {
            user.chargePoint(0)  // 0 포인트 충전 시도
        }

        assertEquals("1보다 작은 포인트는 충전할 수 없습니다.", exception.message)
    }
}
