package net.nickg.charter.rewards;

import net.nickg.charter.rewards.customer.CustomerRewardsController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RewardsApplicationTests {

	@Autowired
	private CustomerRewardsController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
