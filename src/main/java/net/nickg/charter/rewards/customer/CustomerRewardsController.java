package net.nickg.charter.rewards.customer;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerRewardsController {

    private final CustomerRewardsService customerRewardsService;

    public CustomerRewardsController(CustomerRewardsService customerRewardsService) {
        this.customerRewardsService = customerRewardsService;
    }

    @PostMapping("/calculate")
    public List<CustomerRewardsResponse> calculateCustomerRewards(@RequestBody List<Purchase> purchases) {
        return customerRewardsService.calculateRewards(purchases);
    }
}
