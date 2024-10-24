package io.hhplus.concert.presentation.user

import io.hhplus.concert.domain.pointHistory.PointHistoryService
import io.hhplus.concert.domain.user.UserService
import io.hhplus.concert.presentation.user.request.PointRequest
import io.hhplus.concert.presentation.user.request.UserRequest
import io.hhplus.concert.presentation.user.response.UserResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val service: UserService,
    private val pointHistoryService: PointHistoryService
) {

    @PostMapping
    fun create(@RequestBody request: UserRequest) = service.save(request.name)
        .run { UserResponse(this) }

    @GetMapping("/{id}")
    fun getPoint(userId: Long, request: PointRequest) = ResponseEntity.ok()
        .body(service.getPoint(userId))

    @PatchMapping("/{id}")
    fun chargePoint(userId: Long, request: PointRequest) = ResponseEntity.ok()
        .body(service.chargePoint(userId, request.amount))

    @GetMapping("/{id}/histories")
    fun getPointHistoryList(userId: Long) = ResponseEntity.ok()
        .body(pointHistoryService.getAll(userId))

}