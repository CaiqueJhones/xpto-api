package com.xptosystems.api.city;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xptosystems.api.domain.City;

@Repository
public interface CityRepository extends CrudRepository<City, Integer> {

    @Query("SELECT c FROM cities c WHERE c.capital = TRUE ORDER BY c.name")
    List<City> findOnlyCapitalsOrderByName();
    
    @Query("SELECT new com.xptosystems.api.city.UfCityCountResponse(c.uf, COUNT(c.id)) "
            + "FROM cities c GROUP BY c.uf ORDER BY COUNT(c.id) DESC")
    List<UfCityCountResponse> findByCountGroupByUfOrderDesc(Pageable pageable);
    
    @Query("SELECT new com.xptosystems.api.city.UfCityCountResponse(c.uf, COUNT(c.id)) "
            + "FROM cities c GROUP BY c.uf ORDER BY COUNT(c.id) ASC")
    List<UfCityCountResponse> findByCountGroupByUfOrderAsc(Pageable pageable);
    
    @Query("SELECT c.name FROM cities c WHERE c.uf = :uf")
    List<String> findNameByUf(@Param("uf") String uf);
        
    List<City> findByUfIgnoreCaseContaining(String like);
    
    List<City> findByNameIgnoreCaseContaining(String like);
    
    List<City> findByCapital(boolean like);
    
    List<City> findByNoAccentsIgnoreCaseContaining(String like);
    
    List<City> findByAlternativeNamesIgnoreCaseContaining(String like);
    
    List<City> findByMicroregionIgnoreCaseContaining(String like);    
    
    List<City> findByMesoregionIgnoreCaseContaining(String like);
    
    List<City> findByLat(BigDecimal lat);
    
    List<City> findByLon(BigDecimal lon);
    
    @Query("SELECT COUNT(DISTINCT c.uf) FROM cities c")
    long countByDistinctUf();
    
    @Query("SELECT COUNT(DISTINCT c.name) FROM cities c")
    long countByDistinctName();
    
    @Query("SELECT COUNT(DISTINCT c.capital) FROM cities c")
    long countByDistinctCapital();
    
    @Query("SELECT COUNT(DISTINCT c.noAccents) FROM cities c")
    long countByDistinctNoAccents();
    
    @Query("SELECT COUNT(DISTINCT c.alternativeNames) FROM cities c")
    long countByDistinctAlternativeNames();
    
    @Query("SELECT COUNT(DISTINCT c.lat) FROM cities c")
    long countByDistinctLat();
    
    @Query("SELECT COUNT(DISTINCT c.lon) FROM cities c")
    long countByDistinctLon();
    
    @Query("SELECT COUNT(DISTINCT c.microregion) FROM cities c")
    long countByDistinctMicroregion();
    
    @Query("SELECT COUNT(DISTINCT c.mesoregion) FROM cities c")
    long countByDistinctMesoregion();
    
    default List<UfCityCountResponse> findUfWithLargestAndSmallestCities() {
        List<UfCityCountResponse> list = new ArrayList<>();
        PageRequest request = PageRequest.of(0, 1);
        list.addAll(findByCountGroupByUfOrderDesc(request));
        list.addAll(findByCountGroupByUfOrderAsc(request));
        return list;
    }
    
    default List<UfCityCountResponse> findUf() {
        return findByCountGroupByUfOrderDesc(PageRequest.of(0, 27));
    }
}
