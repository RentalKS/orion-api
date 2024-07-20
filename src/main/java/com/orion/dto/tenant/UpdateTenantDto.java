package com.orion.dto.tenant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTenantDto {
    @NotNull(message = "must.not.be.null")
    Long id;

    @NotBlank(message = "must.not.be.empty")
    private String name;

    @NotBlank(message = "must.not.be.empty")
    private String domain;


    public String getName(){
        if (name != null)
            return name.trim();
        return null;
    }

    public String getDomain(){
        if (domain != null)
            return domain.trim();
        return null;
    }
}
