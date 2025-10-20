package com.orion.generics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseObject {

    private String timestamp;

    private Integer status;

    private String error;

    private Object message;

    private String path;
}
