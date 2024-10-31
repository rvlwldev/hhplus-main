package scheduler

import io.hhplus.concert.domain.payment.Payment
import io.hhplus.concert.domain.payment.PaymentStatus
import io.hhplus.concert.infrastructure.jpa.PaymentJpaRepository
import io.hhplus.concert.infrastructure.task.PaymentScheduler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class PaymentSchedulerTest {

    @Mock
    private lateinit var repo: PaymentJpaRepository

    @InjectMocks
    private lateinit var scheduler: PaymentScheduler

    private lateinit var fakedb: MutableList<Payment>

    @BeforeEach
    fun setUp() {
        fakedb = mutableListOf()

        `when`(repo.findAll()).thenAnswer { fakedb }

        `when`(repo.saveAll(any<List<Payment>>())).thenAnswer { invocation ->
            val payments = invocation.arguments[0] as List<Payment>
            payments.forEach { payment ->
                val existingPayment = fakedb.find { it.id == payment.id }
                if (existingPayment != null) fakedb.remove(existingPayment)
                fakedb.add(payment)
            }
            fakedb
        }

    }

    @Test
    fun `expirePayment - WAIT 상태에서 만료된 결제를 저장한다`() {
        // given
        val payments = List(100) { i ->
            var now = LocalDateTime.now()
            if (i % 2 == 1) now = now.minusHours(1)
            Payment(id = i.toLong(), status = PaymentStatus.WAIT, createdAt = now)
        }
        repo.saveAll(payments)

        // when
        scheduler.expirePayment()

        // then
        val size = repo.findAll()
            .filter { it.status == PaymentStatus.TIME_OUT }.size
        assertEquals(50, size)
    }

    @Test
    fun `clearExpiredPayment - TIME_OUT 상태인 결제를 삭제한다`() {
        // given
        val list = mutableListOf<Payment>()
        for (i in 1..50) list.add(Payment(id = i.toLong(), status = PaymentStatus.TIME_OUT))
        for (i in 51..100) list.add(Payment(id = i.toLong(), status = PaymentStatus.PAID))
        for (i in 101..150) list.add(Payment(id = i.toLong(), status = PaymentStatus.WAIT))
        repo.saveAll(list)

        // when
        `when`(repo.deleteAll(any<List<Payment>>())).thenAnswer { invocation ->
            val list = invocation.arguments[0] as List<Payment>
            fakedb.removeAll { payment -> list.any { it.id == payment.id } }
        }

        `when`(repo.findAllByStatus(any<String>())).thenAnswer { invocation ->
            fakedb.forEach { println(it.status) }
            fakedb.filter { it.status.name == invocation.arguments[0] }
        }

        scheduler.clearExpiredPayment()

        // then
        assertEquals(100, repo.findAll().size)
    }
}