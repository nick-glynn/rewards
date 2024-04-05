package net.nickg.charter.rewards.customer;

import java.time.LocalDate;

public record Purchase(int customerId, int amount, LocalDate date) {
}
