package com.orion.util;

import com.orion.enums.vehicle.VehicleStatus;

import java.util.function.Consumer;

public class DtoUtils {
    public static <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

}
