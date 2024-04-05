package net.nickg.charter.rewards;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static net.nickg.charter.rewards.RewardsPointsCalculator.calculateRewardsPoints;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RewardsPointsCalculatorTest {

    @Test
    void calculateRewardsPoints_AmountIsZero_ReturnsZeroPoints() {
        int amount = 0;
        int expectedPoints = 0;
        assertEquals(expectedPoints, calculateRewardsPoints(amount));
    }

    @Test
    void calculateRewardsPoints_NegativeAmount_ReturnsZeroPoints() {
        int amount = -10;
        int expectedPoints = 0;
        assertEquals(expectedPoints, calculateRewardsPoints(amount));
    }

    @Test
    void calculateRewardsPoints_BelowTierOneMinimum_ReturnsZeroPoints() {
        int amount = RewardsPointsCalculator.REWARDS_TIER_1_AMOUNT_MIN - 10;
        int expectedPoints = 0;
        assertEquals(expectedPoints, calculateRewardsPoints(amount));
    }

    @Test
    void calculateRewardsPoints_AtTierOneMinimum_ReturnsZeroPoints() {
        int amount = RewardsPointsCalculator.REWARDS_TIER_1_AMOUNT_MIN;
        int expectedPoints = 0;
        assertEquals(expectedPoints, calculateRewardsPoints(amount));
    }

    @Test
    void calculateRewardsPoints_AboveTierOneMinimum_ReturnsCorrectPoints() {
        int amount = RewardsPointsCalculator.REWARDS_TIER_1_AMOUNT_MIN + 10;
        int expectedPoints = 10;
        assertEquals(expectedPoints, calculateRewardsPoints(amount));
    }

    @Test
    void calculateRewardsPoints_AtTierTwoMinimum_ReturnsCorrectPoints() {
        int amount = RewardsPointsCalculator.REWARDS_TIER_2_AMOUNT_MIN;
        int expectedPoints = RewardsPointsCalculator.REWARDS_TIER_1_POINTS_MAX;
        assertEquals(expectedPoints, calculateRewardsPoints(amount));
    }

    @Test
    void calculateRewardsPoints_AboveTierTwoMinimum_ReturnsCorrectPoints() {
        int amount = RewardsPointsCalculator.REWARDS_TIER_2_AMOUNT_MIN + 20;
        int expectedPoints = RewardsPointsCalculator.REWARDS_TIER_1_POINTS_MAX + (2 * 20);
        assertEquals(expectedPoints, calculateRewardsPoints(amount));
    }

    private static Stream<Arguments> validAmountPointsProvider() {
        return Stream.of(
                // Arguments are in the form of (amount, expectedPoints)
                Arguments.of(0, 0), // amount: 0 - expected points: 0
                Arguments.of(49, 0), // amount: 49 - expected points: 0
                Arguments.of(50, 0), // amount: 50 - expected points: 0
                Arguments.of(70, 20), // amount: 70 - expected points: 20
                Arguments.of(100, 50), // amount: 100 - expected points: 50
                Arguments.of(120, 90)  // amount: 120 - expected points: 90
        );
    }

    private static Stream<Arguments> negativeAmountPointsProvider() {
        return Stream.of(
                // Arguments are in the form of (amount, expectedPoints)
                Arguments.of(-5, 0), // amount: -5 - expected points: 0
                Arguments.of(-1, 0) // amount: -1 - expected points: 0
        );
    }

    @ParameterizedTest
    @MethodSource("validAmountPointsProvider")
    void calculateRewardsPoints_validAmounts_returnCorrectRewardsPoints(int amount, int expectedPoints) {
        int calculatedPoints = calculateRewardsPoints(amount);
        assertEquals(expectedPoints, calculatedPoints);
    }

    @ParameterizedTest
    @MethodSource("negativeAmountPointsProvider")
    void calculateRewardsPoints_negativeAmounts_returnZeroPoints(int amount, int expectedPoints) {
        int calculatedPoints = calculateRewardsPoints(amount);
        assertEquals(expectedPoints, calculatedPoints);
    }
}