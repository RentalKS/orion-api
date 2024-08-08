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
@Table(name = "companies")

public class Company  extends BaseEntity{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "company_name", unique = true)
    private String companyName; //auto sherreti // fron a

    @Column(name= "company_address")
    private String companyAddress;

    @Column(name= "company_email")
    private String companyEmail;

    @Column(name= "company_phone")
    private String companyPhone;

    @Column(name= "company_logo")
    private String companyLogo;

    @Column(name= "zip_code")
    private String zipCode;

    @Column(name= "city")
    private String city;

    @Column(name= "state")
    private String state;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    private Tenant tenant;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
