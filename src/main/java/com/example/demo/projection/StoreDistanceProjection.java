package com.example.demo.projection;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface StoreDistanceProjection {
    Integer getId();
    String getName();
    String getAddress();
    String getPhone();
    String getImage();
    String getCategory();
    Double getDistance(); // 對應 SQL 中的 AS distance
    

    default Double getPrettyDistance() {
        if (getDistance() == null) return 0.0;
        return BigDecimal.valueOf(getDistance())
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();
    }
}