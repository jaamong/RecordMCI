package io.jaamong.recordMCI.domain;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyRecord extends BaseEntity{

    @Column(name = "daily_record_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;  // The date for this record

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users user;  // Which patient this belongs to

    @OneToMany(mappedBy = "dailyRecord")
    private List<Food> foods = new ArrayList<>();  // Foods taken that day

    @OneToMany(mappedBy = "dailyRecord")
    private List<Activity> activities = new ArrayList<>();  // Activities done that day

    @Column(length = 1000, columnDefinition = "TEXT")
    private String memo;  // Memo text (max 1000 chars)

    @Builder
    public DailyRecord(LocalDate date, Users user) {
        this.date = date;
        this.user = user;
    }

    public void addFood(Food food) {
        this.foods.add(food);
        food.mapDailyRecord(this);
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
        activity.mapDailyRecord(this);
    }
}
