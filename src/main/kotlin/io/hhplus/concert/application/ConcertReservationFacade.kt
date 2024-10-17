package io.hhplus.concert.application

import io.hhplus.concert.application.component.TokenProvider
import io.hhplus.concert.domain.concert.ConcertService
import io.hhplus.concert.domain.queue.QueueService
import io.hhplus.concert.domain.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

enum class TokenType {
    QUEUE, PAYMENT
}

data class TokenCommand(
    val token: String,
    val type: TokenType
)

@Service
class ConcertReservationFacade(
    private val tokenProvider: TokenProvider,
    private val userService: UserService,
    private val concertService: ConcertService,
    private val queueService: QueueService,
) {

    @Transactional
    fun reserve(userId: Long, scheduleId: Long): TokenCommand {
        userService.get(userId)
        concertService.getSchedule(scheduleId)

        val queue = queueService.create(userId, scheduleId)
        val token = tokenProvider.createQueueToken(userId, scheduleId, queue.createdAt)

        return TokenCommand(token, TokenType.QUEUE)
    }

    @Transactional
    fun waitQueue(token: String, scheduleId: Long): TokenCommand {
        val claims = tokenProvider.validateQueueToken(token)
        val userId = claims["userId"]!!

        val queue = queueService.get(userId, scheduleId)
        return if (queue.status == "PASS")
            TokenCommand(tokenProvider.createPaymentToken(userId, scheduleId), TokenType.PAYMENT)
        else TokenCommand(token, TokenType.QUEUE)
    }

}