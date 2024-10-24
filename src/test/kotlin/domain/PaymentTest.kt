import io.hhplus.concert.domain.payment.Payment
import io.hhplus.concert.domain.payment.PaymentStatus
import io.hhplus.concert.domain.user.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class PaymentTest {

    @Test
    fun `결제 시간이 초과된 경우 결제 시도 시 예외 발생`() {
        // 결제 생성 시간(createdAt)을 10분 전으로 설정하여 결제 시간이 초과된 상태
        val user = User(id = 1L, name = "Test User")
        val payment = Payment(
            user = user,
            amount = 1000L,
            createdAt = LocalDateTime.now().minusMinutes(10)  // 결제 시간이 초과된 상태
        )

        val exception = assertThrows<IllegalStateException> {
            payment.pay()  // 시간 초과 상태에서 결제를 시도
        }

        assertEquals("결제 대기 시간이 초과되었습니다.\n다시 시도해주세요", exception.message)
    }

    @Test
    fun `이미 성공한 결제에 대해 다시 결제 시도 시 예외 발생`() {
        val user = User(id = 1L, name = "Test User")
        val payment = Payment(user = user, amount = 1000L)

        // 첫 번째 결제 성공
        payment.pay()

        // 두 번째 결제 시도 시 예외 발생
        val exception = assertThrows<IllegalStateException> {
            payment.pay()  // 이미 결제 완료된 상태에서 다시 결제를 시도
        }

        assertEquals("이미 처리된 결제건 입니다.", exception.message)
    }

    @Test
    fun `결제가 완료되지 않은 상태에서 취소 시도 시 예외 발생`() {
        val user = User(id = 1L, name = "Test User")
        val payment = Payment(user = user, amount = 1000L)

        // 결제를 하지 않은 상태에서 취소 시도
        val exception = assertThrows<IllegalStateException> {
            payment.cancel()  // 결제 상태가 SUCCESS가 아닌 상태에서 취소 시도
        }

        assertEquals("처리되지 않은 결제건 입니다.", exception.message)
    }

    @Test
    fun `정상 결제 이후 취소 시 성공`() {
        val user = User(id = 1L, name = "Test User")
        val payment = Payment(user = user, amount = 1000L)

        // 결제 성공
        payment.pay()

        // 결제를 성공한 후 취소 시도
        payment.cancel()

        // 결제 상태가 취소(CANCEL)로 변경되었는지 확인
        assertEquals(PaymentStatus.CANCEL, payment.status)
        assertNotNull(payment.updatedAt)  // 결제가 취소된 시간이 기록되었는지 확인
    }
}
