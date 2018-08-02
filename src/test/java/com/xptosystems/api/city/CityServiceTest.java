package com.xptosystems.api.city;

import static com.xptosystems.api.FakeDomain.city;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.xptosystems.api.FakeDomain;
import com.xptosystems.api.NotFoundException;
import com.xptosystems.api.domain.City;

public class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testListOnlyCapital() {
        given(cityRepository.findOnlyCapitalsOrderByName())
                .willReturn(Arrays.asList(city(), city()));

        List<CityForm> capitals = service.listOnlyCapital();

        assertThat(capitals).size().isEqualTo(2);
    }

    @Test
    public void testListUfWithLargestAndSmallestCities() throws Exception {
        given(cityRepository.findUfWithLargestAndSmallestCities()).willReturn(Arrays.asList(
                new UfCityCountResponse("Teste1", 100), new UfCityCountResponse("Teste2", 1)));
        
        List<UfCityCountResponse> ufs = service.listUfWithLargestAndSmallestCities();
        
        assertThat(ufs).size().isEqualTo(2);
    }
    
    @Test
    public void testListUf() throws Exception {
        given(cityRepository.findUf()).willReturn(Arrays.asList(
                new UfCityCountResponse("Teste1", 100), new UfCityCountResponse("Teste2", 1)));
        
        List<UfCityCountResponse> ufs = service.listUf();
        
        assertThat(ufs).size().isEqualTo(2);
    }
    
    @Test
    public void testFindByIdWithSuccess() throws Exception {
        given(cityRepository.findById(any(Integer.class))).willReturn(Optional.of(city()));
        
        CityForm byId = service.findById(1);
        assertThat(byId).isNotNull();
    }
    
    @Test
    public void testFindByIdWithException() throws Exception {
        given(cityRepository.findById(any(Integer.class))).willReturn(Optional.empty());
        
        assertThatThrownBy(() -> service.findById(1)).isInstanceOf(NotFoundException.class);
    }
    
    @Test
    public void testListNamesByUf() throws Exception {
        given(cityRepository.findNameByUf(any(String.class))).willReturn(Arrays.asList("A", "B", "C"));
        
        List<String> byUf = service.listNamesByUf("BA");
        assertThat(byUf).size().isEqualTo(3);
    }
    
    @Test
    public void testCreateCity() throws Exception {
        given(cityRepository.save(any(City.class))).will(inv -> inv.getArgument(0));
        
        City city = city();
        CityForm cityForm = CityForm.of(city);
        service.createCity(cityForm);
        
        verify(cityRepository).save(city);
    }
    
    @Test
    public void testDeleteByIdWithSuccess() throws Exception {
        given(cityRepository.findById(any(Integer.class))).willReturn(Optional.of(city()));
        
        CityForm byId = service.deletCity(1);
        assertThat(byId).isNotNull();
        
        verify(cityRepository).delete(any(City.class));
    }
    
    @Test
    public void testDeleteByIdWithException() throws Exception {
        given(cityRepository.findById(any(Integer.class))).willReturn(Optional.empty());
        
        assertThatThrownBy(() -> service.deletCity(1)).isInstanceOf(NotFoundException.class);
        
        verify(cityRepository, never()).delete(any(City.class));
    }
    
    @Test
    public void testFilterByColumn() throws Exception {
        service.filterByColumn("ibge_id", "1");
        service.filterByColumn("uf", "BA");
        service.filterByColumn("name", "teste");
        service.filterByColumn("capital", "true");
        service.filterByColumn("no_accents", "teste");
        service.filterByColumn("alternative_names", "teste");
        service.filterByColumn("microregion", "teste");
        service.filterByColumn("mesoregion", "teste");
        service.filterByColumn("lat", "1.00");
        service.filterByColumn("lon", "1.12");
        
        verify(cityRepository).findByUfIgnoreCaseContaining(any(String.class));
        verify(cityRepository).findByNameIgnoreCaseContaining(any(String.class));
        verify(cityRepository).findByCapital(any(Boolean.class));
        verify(cityRepository).findByNoAccentsIgnoreCaseContaining(any(String.class));
        verify(cityRepository).findByAlternativeNamesIgnoreCaseContaining(any(String.class));
        verify(cityRepository).findByMicroregionIgnoreCaseContaining(any(String.class));
        verify(cityRepository).findByMesoregionIgnoreCaseContaining(any(String.class));
        verify(cityRepository).findById(any(Integer.class));
        verify(cityRepository).findByLat(any(BigDecimal.class));
        verify(cityRepository).findByLon(any(BigDecimal.class));
        
        assertThat(service.filterByColumn("test", "teste")).isEmpty();
    }
    
    @Test
    public void testCountByColumn() throws Exception {
        service.countByColumn("ibge_id");
        service.countByColumn("uf");
        service.countByColumn("name");
        service.countByColumn("capital");
        service.countByColumn("no_accents");
        service.countByColumn("alternative_names");
        service.countByColumn("microregion");
        service.countByColumn("mesoregion");
        service.countByColumn("lat");
        service.countByColumn("lon");
        
        verify(cityRepository).count();
        verify(cityRepository).countByDistinctUf();
        verify(cityRepository).countByDistinctName();
        verify(cityRepository).countByDistinctCapital();
        verify(cityRepository).countByDistinctNoAccents();
        verify(cityRepository).countByDistinctAlternativeNames();
        verify(cityRepository).countByDistinctLat();
        verify(cityRepository).countByDistinctLon();
        verify(cityRepository).countByDistinctMicroregion();
        verify(cityRepository).countByDistinctMesoregion();
        
        assertThat(service.countByColumn("teste")).isZero();
    }
    
    @Test
    public void testListFurther() throws Exception {
        City c1 = FakeDomain.city(); // Rio de Janeiro
        c1.setName("Rio de Janeiro");
        c1.setLat(new BigDecimal("-22.9035"));
        c1.setLon(new BigDecimal("-43.2096"));

        City c2 = FakeDomain.city(); // Sao Paulo
        c2.setName("São Paulo");
        c2.setLat(new BigDecimal("-23.5489"));
        c2.setLon(new BigDecimal("-46.6388"));
        
        City c3 = FakeDomain.city(); // Salvador
        c3.setName("Salvador");
        c3.setLat(new BigDecimal("-12.9704"));
        c3.setLon(new BigDecimal("-38.5124"));
        
        City c4 = FakeDomain.city(); // Porto Alegre
        c4.setName("Porto Alegre");
        c4.setLat(new BigDecimal("-30.0277"));
        c4.setLon(new BigDecimal("-51.2287"));
        
        City c5 = FakeDomain.city(); // Rio Branco
        c5.setName("Rio Branco");
        c5.setLat(new BigDecimal("-9.974"));
        c5.setLon(new BigDecimal("-67.8076"));
        
        List<City> list = Arrays.asList(c1, c2, c3, c4, c5);
        given(cityRepository.findAll()).willReturn(list);
        given(cityRepository.count()).willReturn((long) list.size());
        
        DistanceResponse listFurther = service.listFurther();
        
        assertThat(listFurther.getCityA().getName()).isEqualTo(c3.getName());
        assertThat(listFurther.getCityB().getName()).isEqualTo(c5.getName());
    }
    
    @Test
    public void testListFurtherWithOneCity() throws Exception {
        City c1 = FakeDomain.city(); // Rio de Janeiro
        c1.setName("Rio de Janeiro");
        c1.setLat(new BigDecimal("-22.9035"));
        c1.setLon(new BigDecimal("-43.2096"));
        
        List<City> list = Arrays.asList(c1);
        given(cityRepository.findAll()).willReturn(list);
        given(cityRepository.count()).willReturn((long) list.size());
        
        DistanceResponse listFurther = service.listFurther();
        
        assertThat(listFurther.getCityA().getName()).isEqualTo(c1.getName());
        assertThat(listFurther.getCityB()).isNull();
        assertThat(listFurther.getDistance()).isZero();
    }
    
    @Test
    public void testListFurtherWithZeroCity() throws Exception {      
        List<City> list = Arrays.asList();
        given(cityRepository.findAll()).willReturn(list);
        given(cityRepository.count()).willReturn((long) list.size());
        
        DistanceResponse listFurther = service.listFurther();
        
        assertThat(listFurther.getCityA()).isNull();
        assertThat(listFurther.getCityB()).isNull();
        assertThat(listFurther.getDistance()).isZero();
    }
    
    @Test
    public void testListFurtherWithTwoCities() throws Exception {
        City c1 = FakeDomain.city(); // Rio de Janeiro
        c1.setName("Rio de Janeiro");
        c1.setLat(new BigDecimal("-22.9035"));
        c1.setLon(new BigDecimal("-43.2096"));

        City c2 = FakeDomain.city(); // Sao Paulo
        c2.setName("São Paulo");
        c2.setLat(new BigDecimal("-23.5489"));
        c2.setLon(new BigDecimal("-46.6388"));
             
        List<City> list = Arrays.asList(c1, c2);
        given(cityRepository.findAll()).willReturn(list);
        given(cityRepository.count()).willReturn((long) list.size());
        
        DistanceResponse listFurther = service.listFurther();
        
        assertThat(listFurther.getCityA().getName()).isEqualTo(c1.getName());
        assertThat(listFurther.getCityB().getName()).isEqualTo(c2.getName());
        assertThat(listFurther.getDistance()).isEqualTo(c1.distanceBetween(c2));
    }
}
