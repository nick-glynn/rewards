package net.nickg.charter.rewards.customer;

import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class CustomerRewardsService {
    public static final int REWARDS_AMOUNT_MIN = 50;
    public static final int DOUBLE_REWARD_AMOUNT_MIN = 100;

    public List<CustomerRewardsResponse> calculateRewards(List<Purchase> purchases) {
        Objects.requireNonNull(purchases, "Purchases cannot be null");
        var months = EnumSet.of(Month.JANUARY, Month.FEBRUARY, Month.MARCH);

        Map<Integer, Map<Month, Integer>> rewards = purchases.stream()
                .filter(purchase -> purchase.getAmount() > 50)
                .collect(
                        groupingBy(
                                Purchase::getCustomerId,
                                groupingBy(
                                        purchase -> purchase.getDate().getMonth(),
                                        Collectors.summingInt(this::calculateRewardsPoints)
                                )
                        )
                );

        return rewards.entrySet()
                .stream()
                .map(customerRewards -> {
                    CustomerRewardsResponse customerRewardsResponse = new CustomerRewardsResponse(customerRewards.getKey(), months);
                    customerRewards.getValue().forEach(customerRewardsResponse::addRewardsPoints);
                    return customerRewardsResponse;
                })
                .collect(Collectors.toList());
    }

    private int calculateRewardsPoints(Purchase purchase) {
        int amount = purchase.getAmount();
        int points = 0;

        if (amount > DOUBLE_REWARD_AMOUNT_MIN) {
            return 2 * (amount - DOUBLE_REWARD_AMOUNT_MIN) + REWARDS_AMOUNT_MIN;
        }

        if (amount > REWARDS_AMOUNT_MIN) {
            return (amount - REWARDS_AMOUNT_MIN);
        }

        return points;
    }
}
