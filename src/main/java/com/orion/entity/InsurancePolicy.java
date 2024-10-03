package com.orion.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "insurance_policies")
public class InsurancePolicy extends BaseEntity {

    @Column(name = "policy_number", nullable = false)
    private String policyNumber;

    @Column(name = "provider_name", nullable = false)
    private String providerName;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "coverage_details")
    private String coverageDetails;
    @OneToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", unique = true)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    private Tenant tenant;
}
