package guru.sfg.brewery.controllers;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @Mock
    BeerService beerService;

    @InjectMocks
    BeerController beerController;

    MockMvc mockMvc;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(beerController).build();
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
            beers.add(BeerDto.builder().id(UUID.randomUUID()).build());
            beers.add(BeerDto.builder().id(UUID.randomUUID()).build());

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
                    .andExpect(jsonPath("$.content", hasSize(2)));

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
                    .andExpect(jsonPath("$.content", hasSize(2)));

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
                    .andExpect(jsonPath("$.content", hasSize(2)));

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
                    .andExpect(jsonPath("$.content", hasSize(2)));

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
                    .andExpect(jsonPath("$.content", hasSize(2)));

            then(beerService).should().listBeers(isNull(), any(BeerStyleEnum.class), any(PageRequest.class));
            assertThat(0).isEqualTo(pageRequestCaptor.getValue().getPageNumber());
            assertThat(25).isEqualTo(pageRequestCaptor.getValue().getPageSize());
            assertThat(BeerStyleEnum.IPA).isEqualTo(beerStyleEnumCaptor.getValue());
        }
    }

    @Test
    void getBeerById() throws Exception{
        //given
        BeerDto beerDto = BeerDto.builder().id(UUID.randomUUID()).build();
        given(beerService.findBeerById(any(UUID.class))).willReturn(beerDto);

        mockMvc.perform(get("/api/v1/beer/" + beerDto.getId().toString()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(beerDto.getId().toString())));
    }
}