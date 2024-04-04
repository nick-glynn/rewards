package net.nickg.charter.rewards.customer;

import net.nickg.charter.rewards.RewardsPointsCalculator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CustomerRewardsService {
    public List<CustomerRewardsResponse> calculateRewards(List<Purchase> purchases) {
        Objects.requireNonNull(purchases);

        List<CustomerRewardsResponse> customerRewards = new ArrayList<>();

        for (Purchase purchase : purchases) {
            if (purchase.getAmount() <= RewardsPointsCalculator.REWARDS_TIER_1_AMOUNT_MIN) {
                continue;
            }

            CustomerRewardsResponse customer = customerRewards.stream()
                    .filter(x -> x.getCustomerId() == purchase.getCustomerId())
                    .findFirst()
                    .orElse(new CustomerRewardsResponse(purchase.getCustomerId()));

            if (!customerRewards.contains(customer)) {
                customerRewards.add(customer);
            }
            
            customer.addRewardsPoints(purchase.getDate().getMonth(), RewardsPointsCalculator.calculateRewardsPoints(purchase.getAmount()));
        }

        return customerRewards;
    }
}
