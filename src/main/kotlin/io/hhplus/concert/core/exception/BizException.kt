package io.hhplus.concert.core.exception

import org.springframework.http.HttpStatus

class BizException(
    val code: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    msg: String = "에러가 발생했습니다."
) : RuntimeException(msg)