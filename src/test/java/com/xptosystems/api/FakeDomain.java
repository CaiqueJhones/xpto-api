package com.xptosystems.api;

import java.math.BigDecimal;
import java.util.Locale;

import com.github.javafaker.Faker;
import com.xptosystems.api.domain.City;

public final class FakeDomain {

    private static final Faker faker = new Faker(new Locale("pt-BR"));
    
    public static City city() {
        City city = new City();
        city.setId(faker.number().numberBetween(1, 100000));
        city.setUf(faker.address().stateAbbr());
        city.setName(faker.address().cityName());
        city.setAlternativeNames("");
        city.setCapital(faker.number().numberBetween(0, 1) == 1);
        city.setLat(BigDecimal.valueOf(Double.parseDouble(faker.address().latitude().replaceAll(",", "."))));
        city.setLon(BigDecimal.valueOf(Double.parseDouble(faker.address().longitude().replaceAll(",", "."))));
        city.setMesoregion(faker.address().cityName());
        city.setMicroregion(faker.address().cityName());
        city.setNoAccents(city.getName());
        return city;
    }
}
