package com.orion.infrastructure.tenant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Request {

    private Long id;
    private String hostname;
}
