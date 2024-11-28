import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

const BASE_URL = 'http://localhost:8080/concerts/reserve';
export let successfulRequests = new Counter('successful_requests');

export let options = {
    vus: 100, 
    iterations: 1, 
};

export default function () {
    const userId = __VU; 
    const scheduleId = 11;

    // console.log(`현재 VU ID: ${__VU}, userId: ${userId}`);

    // 예약 생성
    const reservePayload = JSON.stringify({
        userId: userId,
        scheduleId: scheduleId,
    });

    const reserveHeaders = {
        'Content-Type': 'application/json',
    };

    const reserveResponse = http.post(BASE_URL, reservePayload, { headers: reserveHeaders });

    check(reserveResponse, {
        'POST /reserve: 상태코드 201': (res) => res.status === 201,
    });

    let queueToken;
    if (reserveResponse.status === 201) {
        successfulRequests.add(1);
        queueToken = reserveResponse.json('token');
    } else {
        queueToken = null;
    }

    // 예약 상태 확인
    if (queueToken) {
        const headers = { 'Authorization': queueToken };
        const getResponse = http.get(BASE_URL, { headers });

        check(getResponse, {
            'GET /reserve: 상태코드 200': (res) => res.status === 200,
            'GET /reserve: type는 WAIT 또는 PASS': (res) => {
                const type = res.json('type');
                return type === 'WAIT' || type === 'PASS';
            },
        });

        if (getResponse.status === 200) {
            successfulRequests.add(1);
        }
    }

    sleep(1); // 요청 간 지연
}