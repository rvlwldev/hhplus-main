package io.hhplus.concert.domain.payment

import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.schedule.ScheduleRepository
import io.hhplus.concert.domain.user.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * TODO : 241027 Payment 전면 수정(재설계), 플로우가 안맞음
 *
 * 1.
 * Payment 도메인을 없애고
 * Facade 에서 UserPoint-use 와 함께 결제 구현?
 *
 * 2.
 * User 와 포인트를 분리
 * 포인트 관리는 Wallet 으로 변경
 * 히스토리도 쓸때마다 그냥 새로운 로우 생성하고
 * 가져올때 OrderBy로?
 *
 * anyway: application/presentation 네이밍도 수정
 *
 * */

@Service
class PaymentService(
    private val repo: PaymentRepository,
    private val userRepo: UserRepository,
    private val scheduleRepo: ScheduleRepository,
) {
//    fun ready(userId: Long, amount: Long) {}
//    fun getHistoryList(userId: Long) {}

    fun pay(userId: Long, scheduleId: Long, seatNumber: Long): PaymentInfo {
        val user = userRepo.findById(userId)
            ?: throw BizException(HttpStatus.NOT_FOUND)

        val schedule = scheduleRepo.findById(scheduleId)
            ?: throw BizException(HttpStatus.NOT_FOUND)

        val payment = Payment(
            user = user,
            amount = schedule.concert.price,
            createdAt = LocalDateTime.now(),
            status = PaymentStatus.SUCCESS
        )

        return repo.save(payment)
            .run { PaymentInfo(this) }
    }

}