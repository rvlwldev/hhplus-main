package facade

import io.hhplus.concert.ConcertApplication
import io.hhplus.concert.application.payment.PaymentFacade
import io.hhplus.concert.domain.seat.SeatRepository
import io.hhplus.concert.domain.user.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest(classes = [ConcertApplication::class])
@AutoConfigureMockMvc
class PaymentIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var paymentFacade: PaymentFacade

    @MockBean
    private lateinit var seatRepository: SeatRepository

    @MockBean
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {

    }

    @Test
    fun `대기열을 생성할 때, 같은 아이디로 중복 생성하면 DUPLICATED 커스텀 예외 발생`() {

    }

    @Test
    fun `좌석 결제 성공`() {

    }
}
