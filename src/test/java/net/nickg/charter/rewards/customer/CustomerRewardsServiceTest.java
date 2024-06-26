package net.nickg.charter.rewards.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerRewardsServiceTest {

    @Autowired
    private CustomerRewardsService customerRewardsService;

    @Test
    void calculateRewards_nullInput_throwsException() {
        assertThrows(
            NullPointerException.class, () -> {
                customerRewardsService.calculateRewards(null);
            }
        );
    }

    @Test
    void calculateRewards_emptyList_returnsEmptyList() {
        List<Purchase> purchases = new ArrayList<>();
        List<CustomerRewardsResponse> rewards = customerRewardsService.calculateRewards(purchases);
        assertTrue(rewards.isEmpty());
    }

    @Test
    void calculateRewards_purchaseBeforeJanuary_excludedFromProcessing() {
        var purchases = List.of(
                new Purchase(1, 120, LocalDate.of(2023, 12, 10))
        );

        var actual = customerRewardsService.calculateRewards(purchases);

        assertTrue(actual.isEmpty());
    }

    @Test
    void calculateRewards_purchasesIncludeOneBeforeJanuary_returnAllOthers() {
        var purchases = List.of(
                new Purchase(1, 105, LocalDate.of(2023, 12, 10)),
                new Purchase(1, 120, LocalDate.of(2024, 1, 10))
        );

        var actual = customerRewardsService.calculateRewards(purchases);

        assertThat(actual).hasSize(1);
    }

    @Test
    void calculateRewards_purchaseAfterMarch_excludedFromProcessing() {
        var purchases = List.of(
                new Purchase(1, 120, LocalDate.of(2024, 4, 10))
        );

        var actual = customerRewardsService.calculateRewards(purchases);

        assertTrue(actual.isEmpty());
    }

    @Test
    void calculateRewards_validPurchases_returnsCorrectResponse() {
        var purchases = List.of(
                new Purchase(1, 120, LocalDate.of(2024, 1, 10)), // 90 points
                new Purchase(1, 150, LocalDate.of(2024, 2, 10)), // 150 points
                new Purchase(2, 100, LocalDate.of(2024, 1, 20)), // 50 points
                new Purchase(3, 80, LocalDate.of(2024, 3, 1)), // 30 points
                new Purchase(3, 51, LocalDate.of(2024, 1, 10)), // 1 point
                new Purchase(2, 50, LocalDate.of(2024, 2, 5)), // 0 points
                new Purchase(1, 0, LocalDate.of(2024, 3, 10)), // 0 points
                new Purchase(1, 55, LocalDate.of(2024, 2, 10)) // 5 points
        );

        var actual = customerRewardsService.calculateRewards(purchases);

        assertEquals(3, actual.size());

        CustomerRewardsResponse customer1 = actual.get(0);
        CustomerRewardsResponse customer2 = actual.get(1);
        CustomerRewardsResponse customer3 = actual.get(2);

        assertEquals(1, customer1.getCustomerId());
        assertEquals(2, customer2.getCustomerId());
        assertEquals(3, customer3.getCustomerId());
        assertEquals(245, customer1.getOverallTotal());
        assertEquals(50, customer2.getOverallTotal());
        assertEquals(31, customer3.getOverallTotal());
        assertEquals(90, customer1.getMonthlyRewardTotals().get(Month.of(1)));
        assertEquals(155, customer1.getMonthlyRewardTotals().get(Month.of(2)));
        assertEquals(0, customer1.getMonthlyRewardTotals().get(Month.of(3)));
        assertEquals(50, customer2.getMonthlyRewardTotals().get(Month.of(1)));
        assertEquals(0, customer2.getMonthlyRewardTotals().get(Month.of(2)));
        assertEquals(0, customer2.getMonthlyRewardTotals().get(Month.of(3)));
        assertEquals(1, customer3.getMonthlyRewardTotals().get(Month.of(1)));
        assertEquals(0, customer3.getMonthlyRewardTotals().get(Month.of(2)));
        assertEquals(30, customer3.getMonthlyRewardTotals().get(Month.of(3)));
    }

}