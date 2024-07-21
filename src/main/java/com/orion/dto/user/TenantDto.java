package com.orion.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TenantDto {
    @NotBlank(message = "must.not.be.empty")
    private String name;

    @NotBlank(message = "must.not.be.empty")
    private String firstName;

    @NotBlank(message = "must.not.be.empty")
    private String lastName;

    @NotBlank(message = "must.not.be.empty")
    @Email(message = "email.is.not.valid")
    private String email;

    @NotBlank(message = "must.not.be.empty")
    private String domain;

    public String getName(){
        if (name != null)
            return name.trim();
        return null;
    }

    public String getFirstName(){
        if (firstName != null)
            return firstName.trim();
        return null;
    }

    public String getLastName(){
        if (lastName != null)
            return lastName.trim();
        return null;
    }

    public String getEmail(){
        if (email != null)
            return email.trim();
        return null;
    }

    public String getDomain(){
        if (domain != null)
            return domain.trim();
        return null;
    }

}
