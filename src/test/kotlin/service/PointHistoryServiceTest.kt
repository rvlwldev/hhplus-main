package service

import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class PointHistoryServiceTest {

//    @Mock
//    lateinit var pointHistoryRepository: PointHistoryRepository
//
//    @Mock
//    lateinit var userRepository: UserRepository
//
//    @InjectMocks
//    lateinit var pointHistoryService: PointHistoryService
//
//    @Test
//    fun `save point history should create and return point history response`() {
//        // Given
//        val user = User(id = 1L, name = "John Doe")
//        val pointHistory = PointHistory(user = user, amount = 100, type = PointHistoryType.CHARGE)
//
//        // When
//        whenever(userRepository.findById(any())).thenReturn(user)
//        whenever(pointHistoryRepository.save(any<PointHistory>())).thenReturn(pointHistory)
//
//        val response = pointHistoryService.save(1L, 100L, PointHistoryType.CHARGE)
//
//        // Then
//        assertNotNull(response)
//        assertEquals(1L, response.userId)
//        assertEquals(100L, response.amount)
//        assertEquals(PointHistoryType.CHARGE.name, response.type)
//    }
//
//    @Test
//    fun `save point history should throw exception if user not found`() {
//        // Given
//        whenever(userRepository.findById(1L)).thenReturn(null)
//
//        // When & Then
//        val exception = assertThrows<IllegalArgumentException> {
//            pointHistoryService.save(1L, 100L, PointHistoryType.CHARGE)
//        }
//
//        assertEquals("존재하지 않는 유저입니다.", exception.message)
//    }
//
//    @Test
//    fun `get all point histories for user should return list of point history responses`() {
//        // Given
//        val user = User(id = 1L, name = "John Doe")
//        val pointHistories = listOf(
//            PointHistory(user = user, amount = 100L, type = PointHistoryType.CHARGE),
//            PointHistory(user = user, amount = 50L, type = PointHistoryType.USE)
//        )
//
//        // When
//        whenever(pointHistoryRepository.findAllByUserId(1L)).thenReturn(pointHistories)
//
//        val responses = pointHistoryService.getAll(1L)
//
//        // Then
//        assertEquals(2, responses.size)
//        assertEquals(100L, responses[0].amount)
//        assertEquals(50L, responses[1].amount)
//    }

}