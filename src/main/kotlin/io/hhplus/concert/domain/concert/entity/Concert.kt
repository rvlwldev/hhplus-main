package io.hhplus.concert.domain.concert.entity

import io.hhplus.concert.domain.concert.dto.ConcertResponse
import jakarta.persistence.*

@Entity
class Concert(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID 자동 생성
    val concertId: Long = 0L,

    val name: String = "",

    val price: Long = 0L,

    @OneToMany(mappedBy = "concert", cascade = [CascadeType.ALL], orphanRemoval = true)
    val schedules: MutableList<ConcertSchedule> = mutableListOf()
) {
    fun toDTO() = ConcertResponse(this)
}