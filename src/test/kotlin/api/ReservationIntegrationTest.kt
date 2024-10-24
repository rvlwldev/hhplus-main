package api

import io.hhplus.concert.ConcertApplication
import io.hhplus.concert.application.payment.PaymentFacade
import io.hhplus.concert.domain.seat.Seat
import io.hhplus.concert.domain.seat.SeatRepository
import io.hhplus.concert.domain.seat.SeatStatus
import io.hhplus.concert.domain.user.User
import io.hhplus.concert.domain.user.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

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
        val user1 = User(id = 1, name = "TestUser1")
        val seat1 = Seat(id = 1, seatNumber = 1)
        seat1.reserve(user1)

        `when`(userRepository.findById(1)).thenReturn(user1)
        `when`(seatRepository.findByScheduleIdAndNumber(1L, 1)).thenReturn(seat1)
        `when`(paymentFacade.getPayableSeatList(1L)).thenReturn(listOf())
//        `when`(paymentFacade.pay(1L, 1L, 1L)).thenReturn(PaymentResponse(seat1))
    }

    @Test
    fun `결제 가능한 좌석 리스트 조회 성공`() {
        val scheduleId = 1L

        mockMvc.perform(
            get("/pay/$scheduleId/seats")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
    }

    @Test
    fun `좌석 결제 성공`() {
        val userId = 1L
        val scheduleId = 1L
        val seatId = 1L

        mockMvc.perform(
            post("/pay/user/$userId/$scheduleId/seats/$seatId")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.seatNumber").value(seatId))
            .andExpect(jsonPath("$.status").value(SeatStatus.WAIT.toString()))
    }
}
