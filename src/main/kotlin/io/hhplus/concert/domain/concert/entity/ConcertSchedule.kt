package io.hhplus.concert.domain.concert.entity

import io.hhplus.concert.domain.concert.dto.ConcertScheduleResponse
import jakarta.persistence.*
import java.time.ZonedDateTime

@Entity
class ConcertSchedule(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public val scheduleId: Long = 0,

    @ManyToOne
    @JoinColumn(name = "concertId")
    val concert: Concert = Concert(),

    @OneToMany(mappedBy = "concertSchedule", cascade = [CascadeType.ALL], orphanRemoval = true)
    val seats: List<Seat> = listOf(),

    val startDateTime: ZonedDateTime = ZonedDateTime.now(),

    val endDateTime: ZonedDateTime = ZonedDateTime.now(),

    val maximumAudienceCount: Long = 50
) {
    fun toDTO() = ConcertScheduleResponse(this)
}