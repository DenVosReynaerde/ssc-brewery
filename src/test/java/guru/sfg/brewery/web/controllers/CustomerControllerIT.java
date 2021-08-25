package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CustomerControllerIT extends BaseIT {

    @DisplayName("List customers")
    @Nested
    class ListCustomers {

        @ParameterizedTest(name = "{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAdminCustomer")
        void testListCustomerAuth(String user, String pwd) throws Exception {
            mockMvc.perform(get("/customers").with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }

        @Test
        void testListCustomerUserRole() throws Exception {
            mockMvc.perform(get("/customers").with(httpBasic("user", "password")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void testListCustomerNoAuth() throws Exception {
            mockMvc.perform(get("/customers"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Add customers")
    @Nested
    class AddCustomers {

        @Rollback
        @Test
        void processCreationForm() throws Exception {
            mockMvc.perform(post("/customers/new")
                    .param("customerName", "Test Customer")
                    .with(httpBasic("foxtrot", "secret")))
                    .andExpect(status().is3xxRedirection());
        }

        @Rollback
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamNotAdmin")
        void processCreateionFormNotAuth(String user, String pwd) throws Exception{
            mockMvc.perform(post("/customers/new")
                    .param("customerName", "Test Customer")
                    .with(httpBasic(user, pwd)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void processCreationFormNoAuth() throws Exception {
            mockMvc.perform(post("/customers/new")
                    .param("customerName", "Test Customer"))
                    .andExpect(status().isUnauthorized());
        }

    }

}