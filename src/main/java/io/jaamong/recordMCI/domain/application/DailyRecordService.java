package io.jaamong.recordMCI.domain.application;

import io.jaamong.recordMCI.api.dto.request.DailyRecordMemoUpdateServiceRequest;
import io.jaamong.recordMCI.api.dto.response.DailyRecordGetDetailResponse;
import io.jaamong.recordMCI.api.dto.response.DailyRecordGetMonthResponse;
import io.jaamong.recordMCI.domain.dto.Activity;
import io.jaamong.recordMCI.domain.dto.DailyRecord;
import io.jaamong.recordMCI.domain.dto.Food;
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
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public DailyRecordGetDetailResponse getTodayBy(Long userId) {
        log.info("[DailyRecordService] getTodayBy :: start : id={}", userId);

        DailyRecord dailyRecord = dailyRecordRepository
                .findDayBy(userId, LocalDate.now())
                .orElseGet(() -> initCreate(userId, LocalDate.now()));// 오늘 첫 조회라면 DailyRecord 초기화

        log.info("[DailyRecordService] getTodayBy :: finish");

        return DailyRecordGetDetailResponse.from(dailyRecord);
    }

    /**
     * 특정 날짜의 DailyRecord 단일 상세 조회
     *
     * @param userId
     * @param date
     * @return DailyRecord 상세 조회 DTO
     */
    public DailyRecordGetDetailResponse getDayBy(Long userId, LocalDate date) {
        log.info("[DailyRecordService] getDayBy :: start : id={}, recordDate={}", userId, date);

        DailyRecord dailyRecord = dailyRecordRepository
                .findDayBy(userId, date)
                .orElseGet(() -> initCreate(userId, date));// 그날의 첫 조회라면 DailyRecord 초기화;

        log.info("[DailyRecordService] getDayBy :: finish : dailyRecord={}", dailyRecord);
        return DailyRecordGetDetailResponse.from(dailyRecord);
    }

    /**
     * 특절 월(month)의 DailyRecord 조회
     *
     * @param userId
     * @param year
     * @param month
     * @return 일별 DailyRecord 항목 별 상태를 담은 리스트
     */
    public List<DailyRecordGetMonthResponse> getMonthlyBy(Long userId, Integer year, Integer month) {
        log.info("[DailyRecordService] getMonthlyBy :: start");

        // 1. Fetch existing records from DB for this month
        List<DailyRecord> existingRecords = dailyRecordRepository.getMonthBy(userId, year, month);

        // 2. Create map for recordDate -> record: Map(recordDate, record)
        Map<LocalDate, DailyRecord> recordMap = existingRecords.stream()
                .collect(Collectors.toMap(
                        DailyRecord::recordDate, r -> r)
                );

        // 3. Generate all days in the month
        YearMonth yearMonth = YearMonth.of(year, month);
        List<DailyRecordGetMonthResponse> days = new ArrayList<>();

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            DailyRecord record = recordMap.get(date);

            days.add(buildMonthResponse(date, record));
        }

        log.info("[DailyRecordService] getMonthlyBy :: finish (before returning)");
        return days;
    }

    @Transactional
    public String patchMemo(DailyRecordMemoUpdateServiceRequest request) {
        log.info("[DailyRecordService] patchMemo :: start : request={}", request.toString());

        DailyRecordEntity dailyRecordEntity = dailyRecordRepository.getDayById(request.id());
        dailyRecordEntity.updateMemo(request.memo());

        log.info("[DailyRecordService] patchMemo :: finish");
        return dailyRecordEntity.getMemo();
    }

    /**
     * 금일 첫 DailyRecord 생성
     * - Food, Activity 모두 초기 구성으로 생성됨
     * - 해당 클래스 전용 메서드 (private)
     *
     * @param userId
     * @return 영속화된 DailyRecord 엔티티
     */
    private DailyRecord initCreate(Long userId, LocalDate date) {
        log.info("[DailyRecordService] initCreate :: start : id={}", userId);

        UserEntity user = usersRepository.getById(userId);

        DailyRecordEntity dailyRecordEntity = DailyRecordEntity.builder()
                .userEntity(user)
                .recordDate(date)
                .build();

        dailyRecordEntity = dailyRecordRepository.save(dailyRecordEntity);

        List<ActivityEntity> activities = activityService.initCreate(dailyRecordEntity);
        List<FoodEntity> foodEntities = foodService.initCreate(dailyRecordEntity);

        dailyRecordEntity.initFoods(foodEntities);
        dailyRecordEntity.initActivities(activities);

        log.info("[DailyRecordService] initCreate :: finish");

        return dailyRecordEntity.toModel();
    }

    private DailyRecordGetMonthResponse buildMonthResponse(LocalDate date, DailyRecord record) {
        if (record == null) {
            return DailyRecordGetMonthResponse.builder()
                    .date(date)
                    .hasRecord(false)
                    .hasFoodConsumed(false)
                    .hasActivityCompleted(false)
                    .hasMemo(false)
                    .build();
        }

        boolean hasFoodCompleted = record.foods().stream().anyMatch(Food::consumed);
        boolean hasActivityCompleted = record.activities().stream().anyMatch(Activity::completed);
        boolean hasMemo = record.memo() != null && !record.memo().trim().isEmpty();

        return DailyRecordGetMonthResponse.builder()
                .date(date)
                .hasRecord(true)
                .hasFoodConsumed(hasFoodCompleted)
                .hasActivityCompleted(hasActivityCompleted)
                .hasMemo(hasMemo)
                .build();
    }
}
