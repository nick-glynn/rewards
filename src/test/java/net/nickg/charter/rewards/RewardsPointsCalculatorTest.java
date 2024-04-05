package net.nickg.charter.rewards;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RewardsPointsCalculatorTest {

    @ParameterizedTest
    @MethodSource("amountPointsProvider")
    void testCalculateRewardsPoints(int amount, int expectedPoints) {
        int calculatedPoints = RewardsPointsCalculator.calculateRewardsPoints(amount);
        assertEquals(expectedPoints, calculatedPoints);
    }

    private static Stream<Arguments> amountPointsProvider() {
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
}