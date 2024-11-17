package io.hhplus.concert.application.payment

interface DataPlatform {

    fun requestSeat(userId: Long, seatNumber: Long)

}