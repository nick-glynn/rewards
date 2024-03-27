package net.nickg.charter.rewards.customer;

import lombok.Data;

import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

@Data
public class CustomerRewardsResponse {
    private int customerId;
    private Map<Month, Integer> monthlyRewardTotals = new HashMap<>();
    private int overallTotal;

    public CustomerRewardsResponse(int customerId, Set<Month> months) {
        this.customerId = customerId;
        months.forEach(month -> this.monthlyRewardTotals.put(month, 0));
    }

    public void addRewardsPoints(Month month, int points) {
        this.monthlyRewardTotals.merge(month, points, Integer::sum);
        this.overallTotal += points;
    }

}

