package io.hhplus.concert.presentation.user

import io.hhplus.concert.presentation.user.response.PointHistoryResponse
import io.hhplus.concert.presentation.user.response.PointResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController {

    @GetMapping("/{id}/points")
    fun getPoint(@PathVariable("id") userId: Long) = ResponseEntity.ok()
        .body(PointResponse(20000L))


    @GetMapping("/{id}/points/histories")
    fun getHistoryList(@PathVariable("id") userId: Long) = ResponseEntity.ok()
        .body(
            listOf(
                PointHistoryResponse(1, "CHARGE", 25000),
                PointHistoryResponse(2, "USE", 10000)
            )
        )

    @PatchMapping("/{id}/points/charge")
    fun chargePoint(@PathVariable("id") userId: Long) = ResponseEntity.ok()
        .body(PointResponse(10000L))
}