package com.xptosystems.api.city;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UfCityCountResponse {

    @JsonProperty("uf")
    private String uf;
    
    @JsonProperty("numberOfCities")
    private long count;
}
