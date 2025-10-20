package com.orion.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "rate_dates")
public class RateDates extends BaseEntity {

    @Column(name = "daily_rate")
    private Double dailyRate;

    @Column(name = "weekly_rate")
    private Double weeklyRate;

    @Column(name = "monthly_rate")
    private Double monthlyRate;

    @Column(name = "name", unique = true)
    private String name; // e.g., Economy, Luxury, SUV

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    private Tenant tenant;
}