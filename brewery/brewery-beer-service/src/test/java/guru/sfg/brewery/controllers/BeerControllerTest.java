package guru.sfg.brewery.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.BeerPagedList;
import guru.sfg.brewery.model.BeerStyleEnum;
import guru.sfg.brewery.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BeerControllerTest {

    public static final String GALAXY_CAT = "Galaxy Cat";
    public static final String OAC_SPEC = "https://raw.githubusercontent.com/sfg-beer-works/brewery-api/master/spec/openapi.yaml";

    @Mock
    BeerService beerService;

    @InjectMocks
    BeerController beerController;

    MockMvc mockMvc;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    BeerDto validBeer;

    @BeforeEach
    void setUp() {
        validBeer = BeerDto.builder().id(UUID.randomUUID())
                .version(1)
                .beerName("Beer1")
                .beerStyle(BeerStyleEnum.PALE_ALE)
                .price(new BigDecimal("12.99"))
                .quantityOnHand(4)
                .upc(123456789012L)
                .createdDate(OffsetDateTime.now())
                .lastModifiedDate(OffsetDateTime.now())
                .build();

        mockMvc = MockMvcBuilders.standaloneSetup(beerController)
                .setMessageConverters(jacksonDateTimeConverter()).build();
    }

    @DisplayName("List Ops - ")
    @Nested
    public class TestListOperations {

        @Captor
        ArgumentCaptor<String> beerNameCaptor;

        @Captor
        ArgumentCaptor<BeerStyleEnum> beerStyleEnumCaptor;

        @Captor
        ArgumentCaptor<PageRequest> pageRequestCaptor;

        BeerPagedList beerPagedList;

        @BeforeEach
        void setUp() {
            List<BeerDto> beers = new ArrayList<>();
            beers.add(validBeer);
            beers.add(BeerDto.builder().id(UUID.randomUUID())
                    .version(1)
                    .beerName("Beer4")
                    .upc(123123123122L)
                    .beerStyle(BeerStyleEnum.PALE_ALE)
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(66)
                    .createdDate(OffsetDateTime.now())
                    .lastModifiedDate(OffsetDateTime.now())
                    .build());

            beerPagedList = new BeerPagedList(beers, PageRequest.of(1, 1), 2L);

            given(beerService.listBeers(beerNameCaptor.capture(), beerStyleEnumCaptor.capture(),
                    pageRequestCaptor.capture())).willReturn(beerPagedList);
        }

        @DisplayName("Test No Params")
        @Test
        void testNoParams() throws Exception {
            mockMvc.perform(get("/api/v1/beer").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(openApi().isValid(OAC_SPEC));

            then(beerService).should().listBeers(isNull(), isNull(), any(PageRequest.class));
            assertThat(0 ).isEqualTo(pageRequestCaptor.getValue().getPageNumber());
            assertThat(25 ).isEqualTo(pageRequestCaptor.getValue().getPageSize());
        }

        @DisplayName("Test Page Size Param")
        @Test
        void testPageSizeParam() throws Exception {
            mockMvc.perform(get("/api/v1/beer").accept(MediaType.APPLICATION_JSON)
                    .param("pageSize", "200"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(openApi().isValid(OAC_SPEC));

            then(beerService).should().listBeers(isNull(), isNull(), any(PageRequest.class));
            assertThat(0 ).isEqualTo(pageRequestCaptor.getValue().getPageNumber());
            assertThat(200 ).isEqualTo(pageRequestCaptor.getValue().getPageSize());
        }

        @DisplayName("Test Page Param")
        @Test
        void testPageParam() throws Exception {
            mockMvc.perform(get("/api/v1/beer").accept(MediaType.APPLICATION_JSON)
                    .param("pageSize", "200"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(openApi().isValid(OAC_SPEC));

            then(beerService).should().listBeers(isNull(), isNull(), any(PageRequest.class));
            assertThat(0 ).isEqualTo(pageRequestCaptor.getValue().getPageNumber());
            assertThat(200 ).isEqualTo(pageRequestCaptor.getValue().getPageSize());
        }

        @DisplayName("Test Beer Name Param")
        @Test
        void testBeerNameParam() throws Exception {
            mockMvc.perform(get("/api/v1/beer").accept(MediaType.APPLICATION_JSON)
                    .param("beerName", GALAXY_CAT))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(openApi().isValid(OAC_SPEC));

            then(beerService).should().listBeers(anyString(), isNull(), any(PageRequest.class));
            assertThat(0).isEqualTo(pageRequestCaptor.getValue().getPageNumber());
            assertThat(25).isEqualTo(pageRequestCaptor.getValue().getPageSize());
            assertThat(GALAXY_CAT).isEqualToIgnoringCase(beerNameCaptor.getValue());
        }

        @DisplayName("Test Beer Style Param")
        @Test
        void testBeerStyle() throws Exception {
            mockMvc.perform(get("/api/v1/beer").accept(MediaType.APPLICATION_JSON)
                    .param("beerStyle", "IPA"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(openApi().isValid(OAC_SPEC));

            then(beerService).should().listBeers(isNull(), any(BeerStyleEnum.class), any(PageRequest.class));
            assertThat(0).isEqualTo(pageRequestCaptor.getValue().getPageNumber());
            assertThat(25).isEqualTo(pageRequestCaptor.getValue().getPageSize());
            assertThat(BeerStyleEnum.IPA).isEqualTo(beerStyleEnumCaptor.getValue());
        }
    }

    @Test
    void getBeerById() throws Exception{
        //given

        given(beerService.findBeerById(any(UUID.class))).willReturn(validBeer);

        mockMvc.perform(get("/api/v1/beer/" + validBeer.getId().toString()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(validBeer.getId().toString())))
                .andExpect(openApi().isValid(OAC_SPEC));
    }

    private MappingJackson2HttpMessageConverter jacksonDateTimeConverter() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());

        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
}