package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class BeerControllerIT extends BaseIT {

    @Autowired
    BeerRepository beerRepository;

    @DisplayName("Init new form")
    @Nested
    class InitNewForm {

        /**
         * test user admin, the password for this user is created using bCrypt encoder
         *
         * @throws Exception
         */
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void initCreationFormAuth(String user, String pwd) throws Exception {
            mockMvc.perform(get("/beers/new")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk())
                    .andExpect(view().name("beers/createBeer"))
                    .andExpect(model().attributeExists("beer"));
        }


        /**
         * test the user scott, the password is created using LDAP encoder.
         *
         * @throws Exception
         */
        @Test
        void initCreationFormNoAuth() throws Exception {
            mockMvc.perform(get("/beers/new"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("init find beer form")
    @Nested
    class InitFindForm {

        /**
         * test user admin, the password for this user is created using bCrypt encoder
         *
         * @throws Exception
         */
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void initFindBeerFormAuth(String user, String pwd) throws Exception {
            mockMvc.perform(get("/beers/find")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk())
                    .andExpect(view().name("beers/findBeers"))
                    .andExpect(model().attributeExists("beer"));
        }


        /**
         * test the user scott, the password is created using LDAP encoder.
         *
         * @throws Exception
         */
        @Test
        void initFindBeerFormNoAuth() throws Exception {
            mockMvc.perform(get("/beers/find"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("process find beer form")
    @Nested
    class ProcessFindForm {

        /**
         * test user admin, the password for this user is created using bCrypt encoder
         *
         * @throws Exception
         */
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void initProcessBeerFromAuth(String user, String pwd) throws Exception {
            mockMvc.perform(get("/beers").param("beerName","")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }


        /**
         * test the user scott, the password is created using LDAP encoder.
         *
         * @throws Exception
         */
        @Test
        void initProcessBeerFormNoAuth() throws Exception {
            mockMvc.perform(get("/beers"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Get beer by id")
    @Nested
    class GetById {

        /**
         * test user admin, the password for this user is created using bCrypt encoder
         *
         * @throws Exception
         */
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void getBeerByIdAUTH(String user, String pwd) throws Exception{
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/beers/" + beer.getId())
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk())
                    .andExpect(view().name("beers/beerDetails"))
                    .andExpect(model().attributeExists("beer"));
        }


        /**
         * test the user scott, the password is created using LDAP encoder.
         *
         * @throws Exception
         */
        @Test
        void initProcessBeerFormNoAuth() throws Exception {
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/beers/" + beer.getId()))
                    .andExpect(status().isUnauthorized());
        }
    }

}
