package io.jaamong.recordMCI.domain.entity;

import io.jaamong.recordMCI.domain.dto.Activity;
import io.jaamong.recordMCI.domain.dto.DailyRecord;
import io.jaamong.recordMCI.domain.dto.Food;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "daily_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyRecordEntity extends BaseEntity {

    @Column(name = "daily_record_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;  // The date for this record

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private UserEntity userEntity;  // Which patient this belongs to

    @OneToMany(mappedBy = "dailyRecordEntity")
    private List<FoodEntity> foodEntityList;  // Foods taken that day

    @OneToMany(mappedBy = "dailyRecordEntity")
    private List<ActivityEntity> activityEntityList;  // Activities done that day

    @Column(length = 1000, columnDefinition = "TEXT")
    private String memo;  // Memo text (max 1000 chars)

    @Builder
    public DailyRecordEntity(LocalDate date, UserEntity userEntity) {
        this.date = date;
        this.userEntity = userEntity;
    }

    public DailyRecord toModel() {
        return DailyRecord.builder()
                .id(id)
                .date(date)
                .user(userEntity.toModel())
                .foods(foodsFrom(foodEntityList))
                .activities(activitiesFrom(activityEntityList))
                .memo(memo)
                .build();
    }

    public void initFoods(List<FoodEntity> foodEntities) {
        this.foodEntityList = new ArrayList<>(foodEntities);
        for (FoodEntity foodEntity : foodEntities) {
            foodEntity.mapDailyRecord(this);
        }
    }

    public void initActivities(List<ActivityEntity> activities) {
        this.activityEntityList = new ArrayList<>(activities);
        for (ActivityEntity activityEntity : activities) {
            activityEntity.mapDailyRecord(this);
        }
    }

    public void addFood(FoodEntity foodEntity) {
        if (this.foodEntityList == null) {
            this.foodEntityList = new ArrayList<>();
        }
        this.foodEntityList.add(foodEntity);
        foodEntity.mapDailyRecord(this);
    }

    public void addActivity(ActivityEntity activityEntity) {
        if(this.activityEntityList == null) {
            this.activityEntityList = new ArrayList<>();
        }
        this.activityEntityList.add(activityEntity);
        activityEntity.mapDailyRecord(this);
    }

    public List<Food> foodsFrom(List<FoodEntity> foodEntityList) {
        return  foodEntityList.stream()
                .map(FoodEntity::toModel)
                .toList();
    }

    public List<Activity> activitiesFrom(List<ActivityEntity> activityEntityList) {
        return activityEntityList.stream()
                .map(ActivityEntity::toModel)
                .toList();
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }
}
