package com.xptosystems.api.city;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xptosystems.api.NotFoundException;
import com.xptosystems.api.domain.City;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CityService {

    private static final String CSV_FILE_PATH = "./cities.csv";

    @Autowired
    private CityRepository cityRepository;

    @Transactional
    public void readCSV() {
        if (cityRepository.count() > 0) {
            return;
        }

        try (Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            for (CSVRecord csvRecord : csvParser) {
                City city = new City();
                city.setId(Integer.parseInt(csvRecord.get("ibge_id")));
                city.setUf(csvRecord.get("uf"));
                city.setName(csvRecord.get("name"));
                city.setCapital("TRUE".equalsIgnoreCase(csvRecord.get("capital")));
                city.setLon(new BigDecimal(csvRecord.get("lon")));
                city.setLat(new BigDecimal(csvRecord.get("lat")));
                city.setNoAccents(csvRecord.get("no_accents"));
                city.setAlternativeNames(csvRecord.get("alternative_names"));
                city.setMicroregion(csvRecord.get("microregion"));
                city.setMesoregion(csvRecord.get("mesoregion"));

                cityRepository.save(city);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public List<CityForm> listOnlyCapital() {
        return mapCity(cityRepository.findOnlyCapitalsOrderByName());
    }

    public List<UfCityCountResponse> listUfWithLargestAndSmallestCities() {
        return cityRepository.findUfWithLargestAndSmallestCities();
    }

    public List<UfCityCountResponse> listUf() {
        return cityRepository.findUf();
    }

    public CityForm findById(int id) {
        Optional<CityForm> op = cityRepository.findById(id).map(CityForm::of);
        if (op.isPresent()) {
            return op.get();
        }
        throw new NotFoundException("City", id);
    }

    public List<String> listNamesByUf(String uf) {
        return cityRepository.findNameByUf(uf.toUpperCase());
    }

    @Transactional
    @CacheEvict(cacheNames = "further", allEntries = true)
    public CityForm createCity(CityForm form) {
        return CityForm.of(cityRepository.save(form.toCity()));
    }

    @Transactional
    @CacheEvict(cacheNames = "further", allEntries = true)
    public CityForm deletCity(int id) {
        Optional<City> op = cityRepository.findById(id);
        if (op.isPresent()) {
            City toDelete = op.get();
            cityRepository.delete(toDelete);
            return CityForm.of(toDelete);
        }
        throw new NotFoundException("City", id);
    }

    public List<CityForm> filterByColumn(String column, String arg) {
        Map<String, Function<String, List<City>>> mapFun = new HashMap<>();
        mapFun.put("ibge_id", like -> {
            try {
                Optional<City> op = cityRepository.findById(Integer.parseInt(like));
                if (op.isPresent()) {
                    return Arrays.asList(op.get());
                }
                return Collections.emptyList();
            } catch (NumberFormatException e) {
                return Collections.emptyList();
            }
        });
        mapFun.put("uf", cityRepository::findByUfIgnoreCaseContaining);
        mapFun.put("name", cityRepository::findByNameIgnoreCaseContaining);
        mapFun.put("capital", like -> cityRepository.findByCapital("true".equalsIgnoreCase(like)));
        mapFun.put("no_accents", cityRepository::findByNoAccentsIgnoreCaseContaining);
        mapFun.put("alternative_names", cityRepository::findByAlternativeNamesIgnoreCaseContaining);
        mapFun.put("microregion", cityRepository::findByMicroregionIgnoreCaseContaining);
        mapFun.put("mesoregion", cityRepository::findByMesoregionIgnoreCaseContaining);
        mapFun.put("lat", like -> {
            try {
                return cityRepository.findByLat(new BigDecimal(like));
            } catch (NumberFormatException e) {
                return Collections.emptyList();
            }
        });
        mapFun.put("lon", like -> {
            try {
                return cityRepository.findByLon(new BigDecimal(like));
            } catch (NumberFormatException e) {
                return Collections.emptyList();
            }
        });

        Function<String, List<City>> fun = mapFun.get(column.toLowerCase());
        if (fun == null) {
            return Collections.emptyList();
        }

        return mapCity(fun.apply(arg));
    }

    public long countByColumn(String column) {
        Map<String, Supplier<Long>> mapFun = new HashMap<>();
        mapFun.put("ibge_id", cityRepository::count);
        mapFun.put("uf", cityRepository::countByDistinctUf);
        mapFun.put("name", cityRepository::countByDistinctName);
        mapFun.put("capital", cityRepository::countByDistinctCapital);
        mapFun.put("no_accents", cityRepository::countByDistinctNoAccents);
        mapFun.put("alternative_names", cityRepository::countByDistinctAlternativeNames);
        mapFun.put("lat", cityRepository::countByDistinctLat);
        mapFun.put("lon", cityRepository::countByDistinctLon);
        mapFun.put("microregion", cityRepository::countByDistinctMicroregion);
        mapFun.put("mesoregion", cityRepository::countByDistinctMesoregion);

        Supplier<Long> sup = mapFun.get(column.toLowerCase());

        if (sup == null) {
            return 0;
        }

        return sup.get();
    }

    public long count() {
        return cityRepository.count();
    }

    @Cacheable(cacheNames = "further")
    public DistanceResponse listFurther() {
        City[] cities = new City[(int) cityRepository.count()];
        int i = 0;
        for (City c : cityRepository.findAll()) {
            cities[i++] = c;
        }

        if (cities.length < 3) {
            switch (cities.length) {
            case 1:
                return new DistanceResponse(CityForm.of(cities[0]), null, BigDecimal.ZERO);
            case 2:
                return new DistanceResponse(CityForm.of(cities[0]), CityForm.of(cities[1]),
                        cities[0].distanceBetween(cities[1]));
            }
        }

        City c1 = null;
        City c2 = null;
        BigDecimal distance = BigDecimal.ZERO;

        for (int k = 0; k < cities.length - 1; k++) {
            for (int j = k + 1; j < cities.length; j++) {
                BigDecimal d = cities[j].distanceBetween(cities[k]);
                if (d.compareTo(distance) > 0) {
                    distance = d;
                    c1 = cities[k];
                    c2 = cities[j];
                }
            }
        }

        CityForm cf1 = c1 == null ? null : CityForm.of(c1);
        CityForm cf2 = c2 == null ? null : CityForm.of(c2);
        return new DistanceResponse(cf1, cf2, distance);
    }

    private List<CityForm> mapCity(List<City> list) {
        return list.stream().map(CityForm::of).collect(Collectors.toList());
    }
}
