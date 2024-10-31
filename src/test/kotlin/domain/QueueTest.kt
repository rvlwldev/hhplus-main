class QueueTest {

//    @Test
//    fun `대기 시간이 초과되었을 때 pass 호출 시 예외 발생`() {
//        val user = User(id = 1L, name = "TEST")
//        val schedule = Schedule()
//        val queue = Queue(
//            user = user,
//            schedule = schedule,
//            createdAt = LocalDateTime.now().minusMinutes(10)
//        )
//
//        val exception = assertThrows<IllegalStateException> {
//            queue.pass()
//        }
//
//        assertEquals("예약 대기 시간이 만료되었습니다.", exception.message)
//    }
//
//    @Test
//    fun `이미 PASS 상태인 대기열에서 다시 pass 호출 시 실패`() {
//        val user = User(id = 1L, name = "TEST")
//        val schedule = Schedule()
//        val queue = Queue(user = user, schedule = schedule, createdAt = LocalDateTime.now())
//
//        queue.pass()
//
//        val exception = assertThrows<IllegalStateException> {
//            queue.pass()
//        }
//
//        assertEquals("이미 처리된 대기열 입니다.", exception.message)
//    }
//
//    @Test
//    fun `대기 시간이 초과되지 않았을 때 pass가 정상적으로 호출되는지 확인`() {
//        val user = User(id = 1L, name = "TEST")
//        val schedule = Schedule()
//        val queue = Queue(
//            user = user,
//            schedule = schedule,
//            createdAt = LocalDateTime.now()
//        )
//
//        queue.pass()
//
//        assertEquals(QueueStatus.PASS, queue.status)
//        assertNotNull(queue.updatedAt)
//    }
}
