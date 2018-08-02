package com.xptosystems.api.city;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DistanceResponse {

    private CityForm cityA;
    private CityForm cityB;
    private BigDecimal distance;
}
