package com.xptosystems.api.domain;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "cities")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class City extends BaseEntity {

    @Id
    @Column(name = "ibge_id")
    private int id;

    @Column(name = "uf")
    private String uf;

    @Column(name = "name")
    private String name;

    @Column(name = "capital")
    private boolean capital;

    @Column(name = "lon")
    private BigDecimal lon;

    @Column(name = "lat")
    private BigDecimal lat;

    @Column(name = "no_accents")
    private String noAccents;

    @Column(name = "alternative_names")
    private String alternativeNames;

    @Column(name = "microregion")
    private String microregion;

    @Column(name = "mesoregion")
    private String mesoregion;

    /**
     * Medida aproximada para distancia entre dois pontos na superficie da Terra.
     * 
     * @param other Cidade que se deseja comparar
     * @return a distancia em KM aproximado.
     */
    public BigDecimal distanceBetween(City other) {
        double lat2 = toRadians(other.lat.doubleValue());
        double lat1 = toRadians(lat.doubleValue());
        double delta = toRadians(other.lon.subtract(lon).doubleValue());

        double cosS = sin(lat2) * sin(lat1) + cos(lat2) * cos(lat1) * cos(delta);
        double s = acos(cosS);
      
        return BigDecimal.valueOf(s).multiply(new BigDecimal("6371"));
    }
}
