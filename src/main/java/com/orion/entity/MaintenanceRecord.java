package com.orion.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.orion.enums.vehicle.DamageType;
import com.orion.enums.vehicle.VehicleStatus;
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
@Table(name = "maintenance_records")
public class MaintenanceRecord extends BaseEntity {

    @Column(name = "maintenance_date", nullable = false)
    private LocalDateTime maintenanceDate;

    @Column(name = "description")
    private String description;

    @Column(name = "cost")
    private Double cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    @Column(name= "damage_type")
    @Enumerated(EnumType.STRING)
    private DamageType damageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    private Tenant tenant;
}
