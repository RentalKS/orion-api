package com.orion.config.tenant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TenantData {

    private Long id;
    private String hostname;
}
