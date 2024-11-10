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
        val CONNECTION_ERROR = Pair(HttpStatus.NOT_ACCEPTABLE, "대기열 서비스에 접속할 수 없습니다.\n잠시 후 다시 시도해 주세요.")
        val NOT_FOUND = Pair(HttpStatus.NOT_FOUND, "해당 대기열을 찾을 수 없습니다.")
        val DUPLICATED = Pair(HttpStatus.CONFLICT, "이미 다른 대기열 요청이 처리중 입니다.")
        val NO_TIME_TO_JOIN =  Pair(HttpStatus.BAD_REQUEST, "예약가능시간이 아닙니다.")
        val TIME_OUT = Pair(HttpStatus.BAD_REQUEST, "대기열 시간이 초과되었습니다.")
    }

    object Schedule {
        val NOT_FOUND = Pair(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다.")
        val NOT_ENOUGH_AVAILABLE_SEAT = Pair(HttpStatus.BAD_REQUEST, "모든 좌석이 예약되었습니다.")
        val NOT_ALLOWED_CANCEL = Pair(HttpStatus.UNAUTHORIZED, "취소할 수 없습니다.")
        val TOO_MANY_REQUEST = Pair(HttpStatus.TOO_MANY_REQUESTS, "예약 요청이 많아 대기 중입니다.\n잠시 후 다시 시도해 주세요.")
    }

    object Seat {
        val NOT_FOUND = Pair(HttpStatus.NOT_FOUND, "좌석을 찾을 수 없습니다.")
        val NOT_AVAILABLE = Pair(HttpStatus.BAD_REQUEST, "해당 좌석을 이용할 수 없습니다.")
        val ALREADY_RESERVED = Pair(HttpStatus.CONFLICT, "이미 예약된 좌석입니다.")
        val INVALID_SEAT_NUMBER = Pair(HttpStatus.BAD_REQUEST, "잘못된 좌석 번호입니다.")
        val TOO_LATE_TO_RESERVE = Pair(HttpStatus.UNAUTHORIZED, "예약할 수 없습니다. 이미 취소되었거나 결제 대기시간이 초과되었습니다.")
    }

    object Payment {
        val DUPLICATED = Pair(HttpStatus.CONFLICT, "이미 결제한 기록이 존재합니다.")
        val NOT_FOUND = Pair(HttpStatus.NOT_FOUND, "결제를 찾을 수 없습니다.")
        val NOT_ENOUGH = Pair(HttpStatus.BAD_REQUEST, "보유 포인트가 부족합니다.")
        val INVALID_AMOUNT = Pair(HttpStatus.BAD_REQUEST, "올바르지 않은 포인트 사용입니다.")
        val TIME_OUT = Pair(HttpStatus.BAD_REQUEST, "결제 기간이 만료되었습니다.")
        val FAILED = Pair(HttpStatus.INTERNAL_SERVER_ERROR, "결제 처리에 실패했습니다.")
        val ALREADY_IN_PROGRESS = Pair(HttpStatus.INTERNAL_SERVER_ERROR, "다른 유저의 결제가 처리중입니다.")
    }

}
