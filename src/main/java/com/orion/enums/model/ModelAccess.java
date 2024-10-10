package com.orion.enums.model;

import com.orion.enums.BrandAccess;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum ModelAccess {
    RS7(BrandAccess.AUDI),
    A6(BrandAccess.AUDI),
    AMG_GTR(BrandAccess.MERCEDES),
    AMG_G63(BrandAccess.MERCEDES),
    M5(BrandAccess.BMW),
    X5(BrandAccess.BMW),
    CAMRY(BrandAccess.TOYOTA),
    COROLLA(BrandAccess.TOYOTA),
    CIVIC(BrandAccess.HONDA),
    ACCORD(BrandAccess.HONDA),
    F150(BrandAccess.FORD),
    MUSTANG(BrandAccess.FORD),
    CAMARO(BrandAccess.CHEVROLET),
    SILVERADO(BrandAccess.CHEVROLET),
    GOLF(BrandAccess.VOLKSWAGEN),
    JETTA(BrandAccess.VOLKSWAGEN),
    ALTIMA(BrandAccess.NISSAN),
    MAXIMA(BrandAccess.NISSAN),
    SONATA(BrandAccess.HYUNDAI),
    ELANTRA(BrandAccess.HYUNDAI),
    OPTIMA(BrandAccess.KIA);

    private final BrandAccess brandAccessName;

    private static final Map<String, ModelAccess> NAME_TO_ENUM = new HashMap<>();

    static {
        for (ModelAccess model : values()) {
            NAME_TO_ENUM.put(model.name(), model);
        }
    }

    ModelAccess(BrandAccess brandAccess) {
        this.brandAccessName = brandAccess;
    }

    public static ModelAccess getByName(String name) {
        return NAME_TO_ENUM.get(name);
    }

    public BrandAccess getBrand() {
        return brandAccessName;
    }

    public String getModelName() {
        return this.name();
    }

    public static List<String> names(int limit) {
        return Arrays.stream(ModelAccess.values())
                .map(ModelAccess::name)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public static String getAcceptedModelNames() {
        return String.join(", ", names(5));
    }

}
