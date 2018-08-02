package com.xptosystems.api.city;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping("/{id}")
    public CityForm findById(@PathVariable("id") int id) {
        return cityService.findById(id);
    }

    @PostMapping
    public CityForm createCity(@RequestBody @Valid CityForm form) {
        System.out.println(form);
        return cityService.createCity(form);
    }

    @DeleteMapping("/{id}")
    public CityForm deletCity(@PathVariable("id") int id) {
        return cityService.deletCity(id);
    }

    @GetMapping("/capitals")
    public List<CityForm> listOnlyCapital() {
        return cityService.listOnlyCapital();
    }

    @GetMapping("/largest-and-smallest")
    public List<UfCityCountResponse> listUfWithLargestAndSmallestCities() {
        return cityService.listUfWithLargestAndSmallestCities();
    }

    @GetMapping("/states")
    public List<UfCityCountResponse> listUf() {
        return cityService.listUf();
    }

    @GetMapping("/names")
    public List<String> listNamesByUf(@RequestParam(name = "uf", required = true) String uf) {
        return cityService.listNamesByUf(uf);
    }

    @GetMapping("/filter")
    public List<CityForm> filterByColumn(
            @RequestParam(name = "column", required = true) String column,
            @RequestParam(name = "q", required = true) String arg) {
        return cityService.filterByColumn(column, arg);
    }

    @GetMapping("/count-all")
    public long count() {
        return cityService.count();
    }

    @GetMapping("/count")
    public long countByColumn(@RequestParam(name = "column", required = true) String column) {
        return cityService.countByColumn(column);
    }

    @GetMapping("/further")
    public DistanceResponse listFurther() {
        return cityService.listFurther();
    }
}
