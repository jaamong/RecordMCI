package io.jaamong.recordMCI.domain.application;

import io.jaamong.recordMCI.api.dto.response.DailyRecordGetOneResponse;
import io.jaamong.recordMCI.domain.dto.DailyRecord;
import io.jaamong.recordMCI.domain.entity.ActivityEntity;
import io.jaamong.recordMCI.domain.entity.DailyRecordEntity;
import io.jaamong.recordMCI.domain.entity.FoodEntity;
import io.jaamong.recordMCI.domain.entity.UserEntity;
import io.jaamong.recordMCI.domain.repository.DailyRecordRepository;
import io.jaamong.recordMCI.domain.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailyRecordService {

    private final DailyRecordRepository dailyRecordRepository;
    private final ActivityService activityService;
    private final UsersRepository usersRepository;
    private final FoodService foodService;

    /**
     * 오늘의 DailyRecord 단일 상세 조회
     * - DB 조회 시 없으면 당일 첫 조회이므로 오늘 기록을 초기화 생성 한다.
     *
     * @param userId
     * @return DailyRecord 상세 조회 DTO
     */
    public DailyRecordGetOneResponse getTodayBy(Long userId) {
        log.info("[DailyRecordService] getTodayBy :: start : userId={}", userId);

        DailyRecord dailyRecord = dailyRecordRepository
                .findDayBy(userId, LocalDate.now())
                .orElseGet(() -> initCreate(userId));// 오늘 첫 조회라면 DailyRecord 초기화

        log.info("[DailyRecordService] getTodayBy :: finish");

        return DailyRecordGetOneResponse.from(dailyRecord);
    }

    /**
     * 특정 날짜의 DailyRecord 단일 상세 조회
     *
     * @param userId
     * @param date
     * @return DailyRecord 상세 조회 DTO
     */
    public DailyRecordGetOneResponse getDayBy(Long userId, LocalDate date) {
        log.info("[DailyRecordService] getDayBy :: start : userId={}, date={}", userId, date);

        DailyRecord dailyRecord = dailyRecordRepository
                .findDayBy(userId, date)
                .orElseGet(() -> initCreate(userId));// 그날의 첫 조회라면 DailyRecord 초기화;

        log.info("[DailyRecordService] getDayBy :: finish : dailyRecord={}", dailyRecord);
        return DailyRecordGetOneResponse.from(dailyRecord);
    }

    /**
     * 금일 첫 DailyRecord 생성
     * - Food, Activity 모두 초기 구성으로 생성됨
     * - 해당 클래스 전용 메서드 (private)
     *
     * @param userId
     * @return 영속화된 DailyRecord 엔티티
     */
    private DailyRecord initCreate(Long userId) {
        log.info("[DailyRecordService] initCreate :: start : userId={}", userId);

        UserEntity user = usersRepository.getById(userId);

        DailyRecordEntity dailyRecordEntity = DailyRecordEntity.builder()
                .userEntity(user)
                .date(LocalDate.now())
                .build();

        dailyRecordEntity = dailyRecordRepository.save(dailyRecordEntity);

        List<ActivityEntity> activities = activityService.initCreate(dailyRecordEntity);
        List<FoodEntity> foodEntities = foodService.initCreate(dailyRecordEntity);

        dailyRecordEntity.initFoods(foodEntities);
        dailyRecordEntity.initActivities(activities);

        log.info("[DailyRecordService] initCreate :: finish");

        return dailyRecordEntity.toModel();
    }
}
