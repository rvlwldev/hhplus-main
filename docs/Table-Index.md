# 가장 성능 개선이 필요한 쿼리 찾기

> 콘서트의 일정 엔티티
> ```kotlin
> class Schedule(
>     @Id
>     @GeneratedValue(strategy = GenerationType.IDENTITY)
>     val id: Long = 0L,
> 
>     val concertId: Long,
>     val sttAt: LocalDateTime = LocalDateTime.now(),
>     val endAt: LocalDateTime = LocalDateTime.now().plusHours(2),
>     val sttReserveAt: LocalDateTime = sttAt.minusWeeks(1),
>     val endReserveAt: LocalDateTime = sttAt.minusWeeks(1),
>     val maximumReservableCount: Long = 0L,
>     reservableCount: Long
> )
> 
> ```
> 콘서트의 일정 조회 레포지토리
> ```kotlin
> interface ScheduleRepository {
>     fun findById(id: Long): Schedule?
>     fun findAllByConcertId(concertId: Long): List<Schedule>
> }
> ```

> 일정의 좌석 조회 레포지토리
> ```kotlin
> interface SeatRepository {
>     fun findByScheduleIdAndNumber(scheduleId: Long, number: Long): Seat?
>     fun findAllByScheduleId(scheduleId: Long): List<Seat>
> }
> ```
좌석 조회의 경우 대기열 이후 조회가 되지만  
콘서트별 일정은 항상 조회가 가능한 구조이기 때문에  
가장 먼저 성능개선이 시급하다

때문에 일정 조회를 기준으로 먼저 성능을 개선해보도록 하자!

## 1. 현재 조회 성능

현재 10만 개의 콘서트와 각각 5개의 일정, 총 500,000만 개의 더미데이터에서  
콘서트 아이디를 조건으로 일정을 조회 했을 때

| id | select_type | table    | partitions | type | possible_keys | key  | key_len | ref  | rows   | filtered | Extra       |
|----|-------------|----------|------------|------|---------------|------|---------|------|--------|----------|-------------|
| 1  | SIMPLE      | schedule | NULL       | ALL  | NULL          | NULL | NULL    | NULL | 497516 | 10.0     | Using where |

라는 결과가 나오며 실행시간은 0.096이 나왔다.

## 2. 적절한 인덱스 대상 컬럼

일단 당연히 콘서트 아이디를 인덱스를 생성해야한다.  
또한 해당 엔티티 활용할때 분명한 니즈가 있는 여러 시나리오가 생각된다.  
추후를 위해 인덱스를 설정할 때 앞으로 필요할 인덱스들은 미리 만들어 놓자

- 날짜를 기준으로 조회
- 현재 시간을 기준으로 예약 가능 콘서트 일정들을 조회

위 시나리오들의 경우를 충분히 생각할 수 있으며  
이 경우 날짜로 복합인덱스를 활용해야 될꺼같다.

때문에 인덱스를 정리해보자면

1. 콘서트아이디 : 콘서트별 일정 조회용
2. 시작일자 : 날짜별 콘서트 일정 조회용
3. 콘서트아이디, 시작일자 : 콘서트별 특정 일자의 일정 조회용
4. 예약시작일자, 예약종료일자 : 예약일자별 일정 조회용
5. 콘서트아이디, 예약시작일자, 예약종료일자 : 특정 콘서트 일정의 예약일 조회용

이 정도로 나눌 수 있을것 같다.

## 3. 인덱스 설정 이후 조회 성능

| id | select_type | table    | partitions | type | possible_keys                                                                                   | key  | key_len | ref  | rows   | filtered | Extra       |
|----|-------------|----------|------------|------|-------------------------------------------------------------------------------------------------|------|---------|------|--------|----------|-------------|
| 1  | SIMPLE      | schedule | NULL       | ALL  | idx_schedule_concert_id, idx_schedule_concert_id_stt_at, idx_schedule_concert_id_reserve_period | NULL | NULL    | NULL | 497516 | 50.0     | Using where |

이런 explain 결과 나왔으며 실행시간은 0.016이 나왔다.  
적은 데이터양이였지만 같은 쿼리가 실행시간이 80퍼센트 이상으로 빨라져 큰 차이가 났다.