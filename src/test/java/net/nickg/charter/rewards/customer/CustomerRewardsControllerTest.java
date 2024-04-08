package net.nickg.charter.rewards.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerRewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void calculateCustomerRewards_worksAsExpected() throws Exception {
        List<Purchase> purchases = List.of(
                new Purchase(1, 40, LocalDate.of(2024, 1, 10)), // skip
                new Purchase(1, 70, LocalDate.of(2024, 2, 15)), // points: 20
                new Purchase(2, 150, LocalDate.of(2024, 3, 20)) // points: 150
        );

        mockMvc.perform(post("/customer/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJSONAsString(purchases)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].customerId").isNumber())
                .andExpect(jsonPath("$[1].customerId").isNumber())
                .andExpect(jsonPath("$[0].overallTotal").isNumber())
                .andExpect(jsonPath("$[1].overallTotal").isNumber());
    }

    private String toJSONAsString(final Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}