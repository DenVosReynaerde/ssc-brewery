package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerOrderRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BeerRestControllerTest extends BaseIT {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @DisplayName("Delete tests")
    @Nested
    class DeleteTests {

        public Beer beerToDelete() {

            Random random = new Random();

            return beerRepository.saveAndFlush(Beer.builder()
                    .beerName("To delete")
                    .beerStyle(BeerStyleEnum.ALE)
                    .minOnHand(13)
                    .quantityToBrew(200)
                    .upc(String.valueOf(random.nextInt(99999999)))
                    .build());
        }


        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
        void deleteBeerHttpBasicAuthFail(String user, String pwd) throws Exception {
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId())
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerHttpBasicSuccess() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId())
                    .with(httpBasic("foxtrot", "secret")))
                    .andExpect(status().isNoContent());
        }

        @Test
        void deleteBeerNoAuth() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId()))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("List beers test")
    @Nested
    class ListBeers {

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void listBeersAuthSuccess(String user, String pwd) throws Exception {
            mockMvc.perform(get("/api/v1/beer/")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }

        @Test
        void listBeersNoAuth() throws Exception {
            mockMvc.perform(get("/api/v1/beer/"))
                    .andExpect(status().isOk());
        }

    }

    @DisplayName("Get beers tests")
    @Nested
    class GetBeers {

        @ParameterizedTest(name = "{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void getBeerByIdAuthSuccess(String user, String pwd) throws Exception {

            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/api/v1/beer/" + beer.getId())
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }

        @Test
        void getBeerByIdUserNoAuthFail() throws Exception {

            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/api/v1/beer/" + beer.getId()))
                    .andExpect(status().isUnauthorized());
        }

    }

    @DisplayName("Get beer by UPC test")
    @Nested
    class GetBeerByUPC {

        @ParameterizedTest(name = "{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void getBeerByUpcAuthSuccess(String user, String pwd) throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/0631234200036")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }


        @Test
        void getBeerByUpcNoAuthFail() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                    .andExpect(status().isUnauthorized());
        }

    }

    @Test
    void findBeerFormADMIN() throws Exception {
        mockMvc.perform(get("/beers").param("beerName", "")
                .with(httpBasic("foxtrot", "secret")))
                .andExpect(status().isOk());
    }

    @Test
    void saveNewBeer() {
    }

    @Test
    void updateBeer() {
    }

    @Test
    void badReqeustHandler() {
    }
}