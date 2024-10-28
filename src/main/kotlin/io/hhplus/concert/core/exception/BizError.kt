package io.hhplus.concert.core.exception

import org.springframework.http.HttpStatus

class BizError {
    object User {
        val NOT_FOUND = Pair(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.")
        val ACCESS_DENIED = Pair(HttpStatus.FORBIDDEN, "사용자 접근이 거부되었습니다.")
    }

    object Concert {
        val NOT_FOUND = Pair(HttpStatus.NOT_FOUND, "콘서트를 찾을 수 없습니다.")
        val ALREADY_RESERVED = Pair(HttpStatus.CONFLICT, "이미 예약된 좌석입니다.")
        val INVALID_SEAT_NUMBER = Pair(HttpStatus.BAD_REQUEST, "잘못된 좌석 번호입니다.")
    }

    object Queue {
        val TIME_OUT = Pair(HttpStatus.BAD_REQUEST, "대기열 시간이 초과되었습니다.")
    }

    object Schedule {
        val NOT_ENOUGH_AVAILABLE_SEAT = Pair(HttpStatus.BAD_REQUEST, "모든 좌석이 예약되었습니다.")
        val NOT_ALLOWED_CANCEL = Pair(HttpStatus.UNAUTHORIZED, "취소할 수 없습니다.")
    }

    object Seat {
        val NOT_AVAILABLE = Pair(HttpStatus.BAD_REQUEST, "좌석을 이용할 수 없습니다.")
        val ALREADY_RESERVED = Pair(HttpStatus.CONFLICT, "이미 예약된 좌석입니다.")
        val INVALID_SEAT_NUMBER = Pair(HttpStatus.BAD_REQUEST, "잘못된 좌석 번호입니다.")
        val NOT_ALLOW_TO_RESERVE = Pair(HttpStatus.UNAUTHORIZED, "예약할 수 없습니다. 이미 취소되었거나 결제 대기시간이 초과되었습니다.")
    }

    object Payment {
        val NOT_FOUND = Pair(HttpStatus.NOT_FOUND, "결제를 찾을 수 없습니다.")
        val NOT_ENOUGH = Pair(HttpStatus.BAD_REQUEST, "보유 포인트가 부족합니다.")
        val INVALID_AMOUNT = Pair(HttpStatus.BAD_REQUEST, "올바르지 않은 포인트 사용입니다.")
        val TIME_OUT = Pair(HttpStatus.BAD_REQUEST, "결제 기간이 만료되었습니다.")
        val FAILED = Pair(HttpStatus.INTERNAL_SERVER_ERROR, "결제 처리에 실패했습니다.")
    }

}
