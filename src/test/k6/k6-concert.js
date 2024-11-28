import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

const BASE_URL = 'http://localhost:8080/concerts';
export let successfulRequests = new Counter('successful_requests');

export let options = {
    vus: 500,
    duration: '10s',
};

export default function () {
    // 1. 전체 콘서트 조회
    const getAllResponse = http.get(`${BASE_URL}`);

    check(getAllResponse, {
        'GET /concerts: 상태코드 200': (res) => res.status === 200,
        'GET /concerts: 응답이 배열': (res) => Array.isArray(res.json()),
    });

    if (getAllResponse.status === 200) {
        successfulRequests.add(1);
    }

    sleep(1);

    // 2. 특정 콘서트의 일정 조회 
    const concerts = getAllResponse.json();
    if (concerts.length > 0) {
        const concertId = concerts[0].concertId;
        const getResponse = http.get(`${BASE_URL}/${concertId}/schedules`);

        check(getResponse, {
            'GET /concerts/{concertId}: 상태코드 200': (res) => res.status === 200,
            'GET /concerts/{concertId}: 응답이 배열': (res) => Array.isArray(res.json()),
        });

        if (getResponse.status === 200) {
            successfulRequests.add(1);
        }

        sleep(1); // 요청 간 지연
    }
}