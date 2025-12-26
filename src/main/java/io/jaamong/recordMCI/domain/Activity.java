package io.jaamong.recordMCI.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Activity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_record_id")
    private DailyRecord dailyRecord;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;  // "Walk", "Bible transcribe", "Coloring book"

    private boolean completed = false;  // For the checkbox

    // Optional fields - only populated when relevant
    private Integer totalSteps;          // null for non-Walk activities
    private Integer totalHours;          // null for non-Walk activities
    private Integer totalMinutes;        // null for non-Walk activities

    @Builder
    public Activity(DailyRecord dailyRecord, ActivityType activityType, boolean completed, Integer totalSteps, Integer totalHours, Integer totalMinutes) {
        this.dailyRecord = dailyRecord;
        this.activityType = activityType;
        this.completed = completed;
        this.totalSteps = totalSteps;
        this.totalHours = totalHours;
        this.totalMinutes = totalMinutes;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", dailyRecord.id=" + dailyRecord.getId() +
                ", activityType='" + activityType + '\'' +
                ", completed=" + completed +
                ", totalSteps=" + totalSteps +
                ", totalHours=" + totalHours +
                ", totalMinutes=" + totalMinutes +
                '}';
    }

    public void mapDailyRecord(DailyRecord dailyRecord) {
        this.dailyRecord = dailyRecord;
    }
}
