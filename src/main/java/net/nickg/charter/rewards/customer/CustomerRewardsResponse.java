package net.nickg.charter.rewards.customer;

import lombok.Data;

import java.time.Month;
import java.util.HashMap;
import java.util.Map;

@Data
public class CustomerRewardsResponse {
    private int customerId;
    private Map<Month, Integer> monthlyRewardTotals = new HashMap<>();
    private int overallTotal = 0;

    public CustomerRewardsResponse(int customerId) {
        this.customerId = customerId;
        this.monthlyRewardTotals = new HashMap<>();
        this.monthlyRewardTotals.put(Month.JANUARY, 0);
        this.monthlyRewardTotals.put(Month.FEBRUARY, 0);
        this.monthlyRewardTotals.put(Month.MARCH, 0);
    }

    public CustomerRewardsResponse(Map<Integer, Map<Month, Integer>> rewards) {
        rewards.forEach((customerId, monthlyRewards) -> {
            this.customerId = customerId;
            monthlyRewards.forEach(this::addRewardsPoints);
        });
    }

    public void addRewardsPoints(Month month, int points) {
        this.monthlyRewardTotals.merge(month, points, Integer::sum);
        this.overallTotal += points;
    }
}

