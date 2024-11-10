package io.hhplus.concert.presentation.user

import io.hhplus.concert.presentation.user.request.PointRequest
import io.hhplus.concert.presentation.user.request.UserRequest
import io.hhplus.concert.presentation.user.response.PointHistoryResponse
import io.hhplus.concert.presentation.user.response.PointResponse
import io.hhplus.concert.presentation.user.response.UserResponse
import org.springframework.http.ResponseEntity

interface IUserController {

    fun create(request: UserRequest): ResponseEntity<UserResponse>
    fun chargePoint(userId: Long, request: PointRequest): ResponseEntity<PointResponse>
    fun getPoint(userId: Long): ResponseEntity<PointResponse>
    fun getPointHistoryList(userId: Long): ResponseEntity<List<PointHistoryResponse>>

}