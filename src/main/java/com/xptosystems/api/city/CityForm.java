package com.xptosystems.api.city;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xptosystems.api.domain.City;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CityForm {

    @JsonProperty("ibge_id")
    @Min(1)
    private int id;

    @JsonProperty("uf")
    @NotBlank
    private String uf;

    @JsonProperty("name")
    @NotBlank
    private String name;

    @JsonProperty("capital")
    private boolean capital;

    @JsonProperty("lon")
    @NotNull
    private BigDecimal lon;

    @JsonProperty("lat")
    @NotNull
    private BigDecimal lat;

    @JsonProperty("no_accents")
    @NotBlank
    private String noAccents;

    @JsonProperty("alternative_name")
    private String alternativeNames;

    @JsonProperty("microregion")
    @NotBlank
    private String microregion;

    @JsonProperty("mesoregion")
    @NotBlank
    private String mesoregion;
    
    public City toCity() {
        City city = new City();
        city.setId(id);
        city.setUf(uf);
        city.setName(name);
        city.setCapital(capital);
        city.setLon(lon);
        city.setLat(lat);
        city.setNoAccents(noAccents);
        city.setAlternativeNames(alternativeNames);
        city.setMicroregion(microregion);
        city.setMesoregion(mesoregion);
        return city;
    }

    public static CityForm of(City city) {
        return new CityForm(city.getId(), city.getUf(), city.getName(), city.isCapital(),
                city.getLon(), city.getLat(), city.getNoAccents(), city.getAlternativeNames(),
                city.getMicroregion(), city.getMesoregion());
    }
}
