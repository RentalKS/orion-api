package com.orion.enums;

import com.orion.enums.model.ModelAccess;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum BrandAccess {
    AUDI,
    MERCEDES,
    BMW,
    TOYOTA,
    HONDA,
    FORD,
    CHEVROLET,
    VOLKSWAGEN,
    NISSAN,
    HYUNDAI,
    KIA,
    MAZDA,
    SUBARU,
    JEEP,
    GMC,
    RAM,
    DODGE;


    public static List<String> names(int limit) {
        return Arrays.stream(BrandAccess.values())
                .map(BrandAccess::name)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static String getAcceptedBrandNames() {
        return String.join(", ", names(5));
    }
}
