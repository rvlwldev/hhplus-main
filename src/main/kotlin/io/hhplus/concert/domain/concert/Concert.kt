package io.hhplus.concert.domain.concert

import io.hhplus.concert.domain.schedule.Schedule
import jakarta.persistence.*

@Entity
@Table(name = "concert")
class Concert(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "name")
    val name: String = "",

    @OneToMany(mappedBy = "concert", cascade = [CascadeType.ALL], orphanRemoval = true)
    val schedules: List<Schedule> = ArrayList(),

    @Column(name = "maximumAudienceCount")
    val maximumAudienceCount: Long = 50

) {
    fun getScheduleList() = schedules
}