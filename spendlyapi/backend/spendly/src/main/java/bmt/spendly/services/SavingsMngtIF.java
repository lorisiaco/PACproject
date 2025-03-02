package bmt.spendly.services;

import java.util.List;

import bmt.spendly.models.Savings;

public interface SavingsMngtIF {
    Savings createSaving(String username, String name, double amount);
    List<Savings> getUserSavings(String username);
    Savings addFundsToSaving(String username, Long savingId, double amount);
    Savings removeFundsFromSaving(String username, Long savingId, double amount);
    void deleteSaving(String username, Long savingId);
}
