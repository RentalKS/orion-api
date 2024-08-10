package com.orion.entity;

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
@Table(name = "models")
public class Model extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "type")
    private String type; // e.g., Sedan, SUV, etc.

    @Column(name = "seating_capacity")
    private int seatingCapacity;

    @Column(name = "fuel_efficiency")
    private String fuelEfficiency; // e.g., MPG

    @Column(name= "year")
    private String year;

    @Column(name = "model_image")
    private String modelImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    private Tenant tenant;
}
