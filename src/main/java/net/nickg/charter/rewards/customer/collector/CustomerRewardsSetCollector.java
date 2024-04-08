package net.nickg.charter.rewards.customer.collector;

import net.nickg.charter.rewards.customer.CustomerRewardsResponse;

import java.time.Month;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;

public class CustomerRewardsSetCollector {
    private static final EnumSet<Month> months = EnumSet.of(Month.JANUARY, Month.FEBRUARY, Month.MARCH);

    public static final Collector<CustomerRewardsResponse, ?, HashSet<CustomerRewardsResponse>> toCustomerRewardsSet = Collector.of(
            HashSet::new,
            accumulateCustomerRewardsSet(),
            combineCustomerRewardsSets()
    );

    private static BinaryOperator<HashSet<CustomerRewardsResponse>> combineCustomerRewardsSets() {
        return (result, others) -> {
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
}
