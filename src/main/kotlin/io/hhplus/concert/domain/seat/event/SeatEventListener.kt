package io.hhplus.concert.domain.seat.event

import com.fasterxml.jackson.databind.ObjectMapper
import io.hhplus.concert.core.exception.BizException
import io.hhplus.concert.domain.core.outbox.OutBox
import io.hhplus.concert.domain.core.outbox.OutBoxService
import io.hhplus.concert.domain.core.outbox.ProgressStatus
import io.hhplus.concert.domain.seat.Seat
import org.springframework.http.HttpStatus
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class SeatEventListener(
    private val kafka: KafkaTemplate<String, String>,
    private val outboxService: OutBoxService,
    private val objectMapper: ObjectMapper
) {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun saveOutBox(seat: SeatEvent.Init) {
        val outbox = OutBox.create<Seat>(
            "\${topic.seat.reserve}",
            seat.id,
            ProgressStatus.INIT,
            seat.toString()
        )

        outboxService.save(outbox)
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun sendInitEvent(seat: SeatEvent.Init) {
        if (outboxService.isDone(seat.id, "Seat"))
            throw BizException(HttpStatus.CONFLICT, "중복된 요청입니다.")

        val seatJson = objectMapper.writeValueAsString(seat)
        kafka.send("\${topic.seat.reserve}", seatJson)
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun sendConfirmEvent(seat: SeatEvent.Confirm) {
        if (outboxService.isDone(seat.id, "Seat"))
            throw BizException(HttpStatus.CONFLICT, "중복된 요청입니다.")

        val seatJson = objectMapper.writeValueAsString(seat)
        kafka.send("\${topic.seat.pay}", seatJson)
    }

}