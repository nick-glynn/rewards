package net.nickg.charter.rewards.customer;

import net.nickg.charter.rewards.RewardsPointsCalculator;
import net.nickg.charter.rewards.customer.collector.CustomerRewardsSetCollector;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Objects;

@Service
public class CustomerRewardsService {
    private static final EnumSet<Month> months = EnumSet.of(Month.JANUARY, Month.FEBRUARY, Month.MARCH);

    public Collection<CustomerRewardsResponse> calculateRewards(Collection<Purchase> purchases) {
        Objects.requireNonNull(purchases);

        return purchases.parallelStream()
                .filter(purchase -> purchase.amount() > RewardsPointsCalculator.REWARDS_TIER_1_AMOUNT_MIN)
                .filter(purchase -> Objects.nonNull(purchase.date()))
                .filter(purchase -> months.contains(purchase.date().getMonth()))
                .map(this::addPointsToCustomerRewardsResponse)
                .collect(CustomerRewardsSetCollector.toCustomerRewardsSet);
    }

    private CustomerRewardsResponse addPointsToCustomerRewardsResponse(Purchase purchase) {
        var cr = new CustomerRewardsResponse(purchase.customerId(), months);
        cr.addRewardsPoints(purchase.date().getMonth(), RewardsPointsCalculator.calculateRewardsPoints(purchase.amount()));
        return cr;
    }
}
