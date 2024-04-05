package net.nickg.charter.rewards.customer;

import lombok.RequiredArgsConstructor;
import net.nickg.charter.rewards.RewardsPointsCalculator;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Service
public class CustomerRewardsService {
    private final EnumSet<Month> months = EnumSet.of(Month.JANUARY, Month.FEBRUARY, Month.MARCH);

    public List<CustomerRewardsResponse> calculateRewards(List<Purchase> purchases) {
        Objects.requireNonNull(purchases);
        List<CustomerRewardsResponse> customerRewards = new ArrayList<>();

        purchases.stream()
                .filter(purchase -> purchase.amount() > RewardsPointsCalculator.REWARDS_TIER_1_AMOUNT_MIN)
                .filter(purchase -> Objects.nonNull(purchase.date()))
                .filter(purchase -> months.contains(purchase.date().getMonth()))
                .forEach(purchase -> {
                    var customer = customerRewards.stream()
                            .filter(cr -> cr.getCustomerId() == purchase.customerId())
                            .findFirst()
                            .orElseGet(addNewCustomerRewardsSummary(purchase, customerRewards, months));

                    customer.addRewardsPoints(purchase.date().getMonth(), RewardsPointsCalculator.calculateRewardsPoints(purchase.amount()));
                });

        return customerRewards;
    }

    private Supplier<CustomerRewardsResponse> addNewCustomerRewardsSummary(Purchase purchase, List<CustomerRewardsResponse> customerRewards, EnumSet<Month> months) {
        return () -> {
            var cr = new CustomerRewardsResponse(purchase.customerId(), months);
            customerRewards.add(cr);
            return cr;
        };
    }
}
