package net.nickg.charter.rewards.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
import static java.time.Month.MARCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        Collection<Purchase> purchases = new ArrayList<>();
        Collection<CustomerRewardsResponse> rewards = customerRewardsService.calculateRewards(purchases);
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

        var customerRewards = actual.stream().toList();

        assertThat(customerRewards)
                .hasSize(3)
                .anySatisfy(customer1Rewards -> {
                    assertThat(customer1Rewards.getCustomerId()).isEqualTo(1);
                    assertThat(customer1Rewards.getOverallTotal()).isEqualTo(245);
                    assertThat(customer1Rewards.getMonthlyRewardTotals().get(JANUARY)).isEqualTo(90);
                    assertThat(customer1Rewards.getMonthlyRewardTotals().get(FEBRUARY)).isEqualTo(155);
                    assertThat(customer1Rewards.getMonthlyRewardTotals().get(MARCH)).isEqualTo(0);
                })
                .anySatisfy(customer2Rewards -> {
                    assertThat(customer2Rewards.getCustomerId()).isEqualTo(2);
                    assertThat(customer2Rewards.getOverallTotal()).isEqualTo(50);
                    assertThat(customer2Rewards.getMonthlyRewardTotals().get(JANUARY)).isEqualTo(50);
                    assertThat(customer2Rewards.getMonthlyRewardTotals().get(FEBRUARY)).isEqualTo(0);
                    assertThat(customer2Rewards.getMonthlyRewardTotals().get(MARCH)).isEqualTo(0);
                })
                .anySatisfy(customer3Rewards -> {
                    assertThat(customer3Rewards.getCustomerId()).isEqualTo(3);
                    assertThat(customer3Rewards.getOverallTotal()).isEqualTo(31);
                    assertThat(customer3Rewards.getMonthlyRewardTotals().get(JANUARY)).isEqualTo(1);
                    assertThat(customer3Rewards.getMonthlyRewardTotals().get(FEBRUARY)).isEqualTo(0);
                    assertThat(customer3Rewards.getMonthlyRewardTotals().get(MARCH)).isEqualTo(30);
                });
    }

}