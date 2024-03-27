package net.nickg.charter.rewards.customer;

import lombok.Value;

import java.time.LocalDate;

@Value
public class Purchase {
    private int customerId;
    private int amount;
    private LocalDate date;
}
