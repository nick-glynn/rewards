package net.nickg.charter.rewards;

public class RewardsPointsCalculator {
    static final int REWARDS_TIER_1_AMOUNT_MIN = 50;
    static final int REWARDS_TIER_1_POINTS_MAX = 50;
    static final int REWARDS_TIER_2_AMOUNT_MIN = 100;

    public static int calculateRewardsPoints(int amount) {
        int points = 0;

        if (amount > REWARDS_TIER_2_AMOUNT_MIN) {
            return (2 * (amount - REWARDS_TIER_2_AMOUNT_MIN)) + REWARDS_TIER_1_POINTS_MAX;
        }

        if (amount > REWARDS_TIER_1_AMOUNT_MIN) {
            return (amount - REWARDS_TIER_1_AMOUNT_MIN);
        }

        return points;
    }
}

