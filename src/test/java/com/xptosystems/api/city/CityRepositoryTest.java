package com.xptosystems.api.city;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xptosystems.api.FakeDomain;
import com.xptosystems.api.domain.City;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CityRepositoryTest {

    @Autowired
    private CityRepository repository;

    public void tearDown() throws Exception {
        repository.deleteAll();
    }

    @Test
    public void testFindUfWithLargestAndSmallestCities() {
        for (int i = 0; i < 3; i++) {
            City city = FakeDomain.city();
            city.setUf("MG");
            repository.save(city);
        }
        City city = FakeDomain.city();
        city.setUf("BA");
        repository.save(city);

        List<UfCityCountResponse> cities = repository.findUfWithLargestAndSmallestCities();
        assertThat(cities).size().isEqualTo(2);
        assertThat(cities.get(0)).isEqualTo(new UfCityCountResponse("MG", 3));
        assertThat(cities.get(1)).isEqualTo(new UfCityCountResponse("BA", 1));
    }

    @Test
    public void testFindNameByUf() throws Exception {
        for (int i = 0; i < 3; i++) {
            City city = FakeDomain.city();
            city.setUf("MG");
            repository.save(city);
        }

        List<String> names = repository.findNameByUf("MG");
        assertThat(names).size().isEqualTo(3);
    }
    
    @Test
    public void testFindByUfIgnoreCaseContaining() throws Exception {
        City c1 = FakeDomain.city();
        c1.setUf("MG");
        repository.save(c1);

        City c2 = FakeDomain.city();
        c2.setUf("BA");
        repository.save(c2);

        List<City> l1 = repository.findByUfIgnoreCaseContaining("B");
        List<City> l2 = repository.findByUfIgnoreCaseContaining("mg");

        assertThat(l1).size().isEqualTo(1);
        assertThat(l2).size().isEqualTo(1);
    }

    @Test
    public void testFindByNoAccentsIgnoreCaseContaining() throws Exception {
        City c1 = FakeDomain.city();
        c1.setNoAccents("Teste");
        repository.save(c1);

        List<City> l1 = repository.findByNoAccentsIgnoreCaseContaining("teste");

        assertThat(l1).size().isEqualTo(1);
    }
    
    @Test
    public void testFindByLat() throws Exception {
        City c1 = FakeDomain.city();
        c1.setLat(new BigDecimal("30.1010"));
        repository.save(c1);

        List<City> l1 = repository.findByLat(new BigDecimal("30.1010"));

        assertThat(l1).size().isEqualTo(1);
    }

    @Test
    public void testFindByCapital() throws Exception {
        City c1 = FakeDomain.city();
        c1.setUf("MG");
        c1.setCapital(true);
        repository.save(c1);

        List<City> l1 = repository.findByCapital(true);

        assertThat(l1).size().isEqualTo(1);
    }

    @Test
    public void testCountByDistinctUf() throws Exception {
        for (int i = 0; i < 3; i++) {
            City city = FakeDomain.city();
            city.setUf("MG");
            repository.save(city);
        }
        City city = FakeDomain.city();
        city.setUf("BA");
        repository.save(city);

        long count = repository.countByDistinctUf();
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void testCountByDistinctCapital() throws Exception {
        for (int i = 0; i < 3; i++) {
            City city = FakeDomain.city();
            city.setCapital(true);
            repository.save(city);
        }
        City city = FakeDomain.city();
        city.setCapital(false);
        repository.save(city);

        long count = repository.countByDistinctCapital();
        assertThat(count).isEqualTo(2);
    }

}
