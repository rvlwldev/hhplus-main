package scheduler

import io.hhplus.concert.domain.queue.Queue
import io.hhplus.concert.domain.queue.QueueStatus
import io.hhplus.concert.infrastructure.jpa.QueueJpaRepository
import io.hhplus.concert.infrastructure.task.QueueScheduler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.lenient
import org.mockito.Mockito.spy
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.data.domain.PageRequest
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class QueueSchedulerTest {

    @Mock
    private lateinit var repo: QueueJpaRepository

    @InjectMocks
    private lateinit var sut: QueueScheduler

    private lateinit var fakedb: MutableList<Queue>

    @BeforeEach
    fun setUp(): Unit {
        fakedb = mutableListOf()

        lenient().`when`(repo.findAll()).thenReturn(fakedb)

        lenient().`when`(repo.findOldestWaitingQueueTopBy(any())).thenAnswer { invocation ->
            fakedb.filter { it.status == QueueStatus.WAIT }
                .sortedBy { it.createdAt }
                .take((invocation.arguments[0] as PageRequest).pageSize)
        }

        lenient().`when`(repo.findAllPassQueue()).thenAnswer {
            val queues = fakedb.filter { it.status == QueueStatus.PASS }
            queues
        }

        lenient().`when`(repo.saveAll(any<List<Queue>>())).thenAnswer { invocation ->
            val queues = invocation.arguments[0] as List<Queue>
            queues.forEach { queue ->
                val existingQueue = fakedb.find { it.id == queue.id }
                if (existingQueue == null) fakedb.add(queue)
            }
            queues
        }

        lenient().`when`(repo.deleteAll(any<List<Queue>>())).thenAnswer { invocation ->
            val queuesToDelete = invocation.arguments[0] as List<Queue>
            fakedb.removeAll { queue -> queuesToDelete.any { it.id == queue.id } }
        }

    }

    @Test
    fun `스케줄러는 50개 넘게 대기열을 pass 시키지 않는다`() {
        // given
        val waits = List(999) { i -> Queue(id = i.toLong()) }
        repo.saveAll(waits)

        // when
        sut.passQueues()

        // then
        assertEquals(repo.findAll().count { it.status == QueueStatus.PASS }, 50)
    }

    @Test
    fun `pass 메서드로 오래된 패스 대기열은 삭제한다`() {
        val queues = List(100) { spy(Queue(id = it.toLong())) }
        repo.saveAll(queues)

        queues.forEach { queue ->
            queue.pass()
            doReturn(queue.id % 2 == 1L).`when`(queue).isTimeOut()
        }

        sut.expirePassQueues()

        assertEquals(repo.findAll().count(), 50)
    }


}