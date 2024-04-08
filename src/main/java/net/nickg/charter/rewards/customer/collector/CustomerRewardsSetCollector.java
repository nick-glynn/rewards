package net.nickg.charter.rewards.customer.collector;

import net.nickg.charter.rewards.RewardsPointsCalculator;
import net.nickg.charter.rewards.customer.CustomerRewardsResponse;
import net.nickg.charter.rewards.customer.Purchase;

import java.time.Month;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;

public class CustomerRewardsSetCollector {
    private static final EnumSet<Month> months = EnumSet.of(Month.JANUARY, Month.FEBRUARY, Month.MARCH);
    private static final Collector<CustomerRewardsResponse, ?, HashSet<CustomerRewardsResponse>> toCustomerRewardsSet = Collector.of(
            HashSet::new,
            accumulateCustomerRewardsSet(),
            combineCustomerRewardsSets()
    );

    private static BinaryOperator<HashSet<CustomerRewardsResponse>> combineCustomerRewardsSets() {
        return (result, others) -> {
            // find the customers in resultSet that matches the customers in others and add the points
            others.forEach(other -> {
                result.stream()
                        .filter(resultCustomerRewards -> resultCustomerRewards.getCustomerId() == other.getCustomerId())
                        .findFirst()
                        .map(resultCustomerRewards -> {
                            resultCustomerRewards.addRewardsPoints(other.getMonthlyRewardTotals());
                            return resultCustomerRewards;
                        })
                        .orElseGet(() -> {
                            result.add(other);
                            return null;
                        });
            });
            return result;
        };
    }

    private static BiConsumer<HashSet<CustomerRewardsResponse>, CustomerRewardsResponse> accumulateCustomerRewardsSet() {
        return (list, cr) -> {
            list.stream()
                    .filter(c -> c.getCustomerId() == cr.getCustomerId())
                    .findFirst()
                    .orElseGet(() -> {
                        var newCustomerRewards = new CustomerRewardsResponse(cr.getCustomerId(), months);
                        list.add(newCustomerRewards);
                        return newCustomerRewards;
                    })
                    .addRewardsPoints(cr.getMonthlyRewardTotals());
        };
    }

    public Collection<CustomerRewardsResponse> calculateRewards(Collection<Purchase> purchases) {
        Objects.requireNonNull(purchases);

        return purchases.parallelStream()
                .filter(purchase -> purchase.amount() > RewardsPointsCalculator.REWARDS_TIER_1_AMOUNT_MIN)
                .filter(purchase -> Objects.nonNull(purchase.date()))
                .filter(purchase -> months.contains(purchase.date().getMonth()))
                .map(this::addPointsToCustomerRewardsResponse)
                .collect(toCustomerRewardsSet);
    }

    private CustomerRewardsResponse addPointsToCustomerRewardsResponse(Purchase purchase) {
        var cr = new CustomerRewardsResponse(purchase.customerId(), months);
        cr.addRewardsPoints(purchase.date().getMonth(), RewardsPointsCalculator.calculateRewardsPoints(purchase.amount()));
        return cr;
    }
}
