package io.hhplus.concert.domain.payment

import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.concert.repository.SeatRepository
import io.hhplus.concert.domain.payment.dto.PaymentResponse
import io.hhplus.concert.domain.payment.entity.PayStatus
import io.hhplus.concert.domain.payment.entity.Payment
import io.hhplus.concert.domain.queue.QueueRepository
import io.hhplus.concert.domain.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZoneId
import java.time.ZonedDateTime

@Service
class PaymentService(
    private val repo: PaymentRepository,
    private val queueRepository: QueueRepository,
    private val seatRepository: SeatRepository,
    private val userService: UserService
) {
    private val NOT_FOUND_MESSAGE = "존재하지 않는 결제 정보입니다"

    fun get(paymentId: Long) = repo.findById(paymentId)
        ?: throw BizException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE)

    fun getList(userId: Long) = repo.findAllByUserId(userId)

    @Transactional
    fun pay(userId: Long, scheduleId: Long, seatId: Long): PaymentResponse {
        val queue = queueRepository.findPassQueueByUserIdAndScheduleId(userId, scheduleId)
            ?: throw BizException(HttpStatus.REQUEST_TIMEOUT, "요청 시간이 초과되었습니다")

        val seat = seatRepository.findByUserIdAndScheduleIdWithLock(userId, scheduleId)
            ?: throw BizException(HttpStatus.BAD_REQUEST, "예약 가능한 좌석이 없습니다.")

        seat.confirm()
        seatRepository.save(seat)

        val seatPrice = 70000L
        userService.use(userId, seatPrice)

        // 4. 결제 정보 저장
        val payment = Payment(
            user = userService.get(userId),
            status = PayStatus.SUCCESS,
            point = seatPrice,
            createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        )
        repo.save(payment)

        queueRepository.deleteByUserIdAndScheduleId(userId, scheduleId)

        return payment.toDTO()
    }

}