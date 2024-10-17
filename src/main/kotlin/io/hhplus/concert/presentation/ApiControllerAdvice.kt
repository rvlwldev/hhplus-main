package io.hhplus.concert.presentation

import io.hhplus.concert.core.exception.BizException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

data class ErrorResponse(val code: String = "500", val message: String = "에러가 발생했습니다.")

@RestControllerAdvice
class ApiControllerAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(BizException::class)
    fun handleIllegalArgumentException(e: BizException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse("${HttpStatus.BAD_REQUEST.value()}", e.message ?: "잘못된 요청입니다.")
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

}