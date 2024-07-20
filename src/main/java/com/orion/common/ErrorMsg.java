package com.orion.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorMsg {

    private String name;

    private String message;

    private Long row;

    public ErrorMsg(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public ErrorMsg(String name, String message, Long row) {
        this.name = name;
        this.message = message;
        this.row = row;
    }
}
