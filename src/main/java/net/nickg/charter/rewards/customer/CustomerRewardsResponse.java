package net.nickg.charter.rewards.customer;

import lombok.Data;

import java.time.Month;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Data
public class CustomerRewardsResponse {
    private int customerId;
    private Map<Month, Integer> monthlyRewardTotals;
    private int overallTotal = 0;

    public CustomerRewardsResponse(int customerId, EnumSet<Month> months) {
        this.customerId = customerId;
        initializeMonthlyRewardsTotals(months);
    }

    public void addRewardsPoints(Month month, int points) {
        this.monthlyRewardTotals.merge(month, points, Integer::sum);
        this.overallTotal += points;
    }

    private void initializeMonthlyRewardsTotals(EnumSet<Month> months) {
        this.monthlyRewardTotals = new HashMap<>();
        months.forEach(month -> this.monthlyRewardTotals.put(month, 0));
    }
}

