package io.hhplus.concert.core.exception

import org.springframework.http.HttpStatus

class BizException(
    val status: HttpStatus,
    msg: String
) : RuntimeException(msg) {
    constructor(error: Pair<HttpStatus, String>) : this(error.first, error.second)
}