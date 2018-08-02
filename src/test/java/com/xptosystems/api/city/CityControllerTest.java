package com.xptosystems.api.city;

import static com.xptosystems.api.FakeDomain.city;
import static com.xptosystems.api.Util.asJson;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.xptosystems.api.NotFoundException;
import com.xptosystems.api.WebTestConfig;
import com.xptosystems.api.domain.City;

public class CityControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CityService cityService;

    @InjectMocks
    private CityController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setHandlerExceptionResolvers(WebTestConfig.restErrorHandler())
                .setLocaleResolver(WebTestConfig.fixedLocaleResolver(Locale.getDefault()))
                .setMessageConverters(WebTestConfig.jacksonDateTimeConverter())
                .setValidator(WebTestConfig.validator())
                .setCustomArgumentResolvers(WebTestConfig.pageRequestArgumentResolver()).build();
    }

    @Test
    public void testFindByIdWithSuccess() throws Exception {
        City city = city();
        given(cityService.findById(1)).willReturn(CityForm.of(city));

        mockMvc.perform(get("/1")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.ibge_id").value(city.getId()))
                .andExpect(jsonPath("$.uf").value(city.getUf()))
                .andExpect(jsonPath("$.name").value(city.getName()))
                .andExpect(jsonPath("$.capital").value(city.isCapital()))
                .andExpect(jsonPath("$.lon").value(city.getLon()))
                .andExpect(jsonPath("$.lat").value(city.getLat()))
                .andExpect(jsonPath("$.no_accents").value(city.getNoAccents()))
                .andExpect(jsonPath("$.alternative_name").value(city.getAlternativeNames()))
                .andExpect(jsonPath("$.microregion").value(city.getMicroregion()))
                .andExpect(jsonPath("$.mesoregion").value(city.getMesoregion()));
    }

    @Test
    public void testFindByIdWithException() throws Exception {
        given(cityService.findById(1)).willThrow(NotFoundException.class);

        mockMvc.perform(get("/1")).andExpect(status().isNotFound());
    }

    @Test
    public void testCreateCityWithSuccess() throws Exception {
        CityForm form = CityForm.of(city());
        given(cityService.createCity(form)).willReturn(form);

        mockMvc.perform(post("").content(asJson(form)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.ibge_id").value(form.getId()))
                .andExpect(jsonPath("$.uf").value(form.getUf()))
                .andExpect(jsonPath("$.name").value(form.getName()))
                .andExpect(jsonPath("$.capital").value(form.isCapital()))
                .andExpect(jsonPath("$.lon").value(form.getLon()))
                .andExpect(jsonPath("$.lat").value(form.getLat()))
                .andExpect(jsonPath("$.no_accents").value(form.getNoAccents()))
                .andExpect(jsonPath("$.alternative_name").value(form.getAlternativeNames()))
                .andExpect(jsonPath("$.microregion").value(form.getMicroregion()))
                .andExpect(jsonPath("$.mesoregion").value(form.getMesoregion()));
    }

    @Test
    public void testCreateCityWithException() throws Exception {
        CityForm form = CityForm.of(city());
        form.setName("");
        given(cityService.createCity(form)).willThrow(NotFoundException.class);

        mockMvc.perform(post("").content(asJson(form)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteWithSuccess() throws Exception {
        City city = city();
        given(cityService.deletCity(1)).willReturn(CityForm.of(city));

        mockMvc.perform(delete("/1")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.ibge_id").value(city.getId()))
                .andExpect(jsonPath("$.uf").value(city.getUf()))
                .andExpect(jsonPath("$.name").value(city.getName()))
                .andExpect(jsonPath("$.capital").value(city.isCapital()))
                .andExpect(jsonPath("$.lon").value(city.getLon()))
                .andExpect(jsonPath("$.lat").value(city.getLat()))
                .andExpect(jsonPath("$.no_accents").value(city.getNoAccents()))
                .andExpect(jsonPath("$.alternative_name").value(city.getAlternativeNames()))
                .andExpect(jsonPath("$.microregion").value(city.getMicroregion()))
                .andExpect(jsonPath("$.mesoregion").value(city.getMesoregion()));
    }

    @Test
    public void testDeleteWithException() throws Exception {
        given(cityService.deletCity(1)).willThrow(NotFoundException.class);

        mockMvc.perform(delete("/1")).andExpect(status().isNotFound());
    }
}
