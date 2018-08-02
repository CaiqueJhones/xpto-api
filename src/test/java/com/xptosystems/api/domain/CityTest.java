package com.xptosystems.api.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

import com.xptosystems.api.FakeDomain;

public class CityTest {

    @Test
    public void testDistanceBetween() {
        City c1 = FakeDomain.city();
        c1.setLat(new BigDecimal("-22.9035"));
        c1.setLon(new BigDecimal("-43.2096"));

        City c2 = FakeDomain.city();
        c2.setLat(new BigDecimal("-23.5489"));
        c2.setLon(new BigDecimal("-46.6388"));

        assertThat(c1.distanceBetween(c2)).isBetween(new BigDecimal("357"), new BigDecimal("358"));
        assertThat(c1.distanceBetween(c1)).isEqualTo("0.0");
    }

}
