package bmt.spendly.models;

public interface BudgetIF {
    Long getId();
    AppUser getUser();
    void setUser(AppUser user);
    double getAmount();
    void setAmount(double amount);
}
