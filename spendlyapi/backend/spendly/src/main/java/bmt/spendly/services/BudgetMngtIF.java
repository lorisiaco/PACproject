package bmt.spendly.services;

import bmt.spendly.models.Budget;

public interface BudgetMngtIF {
    Budget createOrGetUserBudget(String username, double initialAmount);
    Budget getUserBudget(String username);
    Budget addFunds(String username, double amountToAdd);
    Budget subtractFunds(String username, double amountToSubtract);
}
