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
@Table(name = "companies")

public class Company  extends BaseEntity{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "company_name", unique = true)
    private String companyName;

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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        if(companyPhone == null || companyPhone.isEmpty()){
            return;
        }
        this.companyPhone = companyPhone;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        if(companyLogo == null || companyLogo.isEmpty()){
            return;
        }
        this.companyLogo = companyLogo;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        if(zipCode == null || zipCode.isEmpty()){
            return;
        }
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if(city == null || city.isEmpty()){
            return;
        }
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        if(state == null || state.isEmpty()){
            return;
        }
        this.state = state;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
