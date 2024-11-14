package io.hhplus.concert.presentation.user

import io.hhplus.concert.domain.pointHistory.PointHistoryService
import io.hhplus.concert.domain.user.UserService
import io.hhplus.concert.presentation.user.request.PointRequest
import io.hhplus.concert.presentation.user.request.UserRequest
import io.hhplus.concert.presentation.user.response.PointHistoryResponse
import io.hhplus.concert.presentation.user.response.PointResponse
import io.hhplus.concert.presentation.user.response.UserResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/users")
class UserController(
    private val service: UserService,
    private val pointHistoryService: PointHistoryService
) : IUserController {

    @PostMapping
    override fun create(@RequestBody request: UserRequest) =
        service.save(request.name)
            .run { UserResponse(this) }
            .run { ResponseEntity.created(URI.create("/users/${this.id}")).body(this) }

    @PatchMapping("/{userId}")
    override fun chargePoint(@PathVariable userId: Long, @RequestBody request: PointRequest) =
        service.chargePoint(userId, request.amount)
            .run { PointResponse(this.point) }
            .run { ResponseEntity.ok(this) }

    @GetMapping("/{userId}")
    override fun getPoint(@PathVariable userId: Long) =
        service.getPoint(userId)
            .run { PointResponse(this) }
            .run { ResponseEntity.ok(this) }

    @GetMapping("/{userId}/histories")
    override fun getPointHistoryList(@PathVariable userId: Long) =
        pointHistoryService.getAll(userId)
            .map { PointHistoryResponse(it) }
            .run { ResponseEntity.ok(this) }

}