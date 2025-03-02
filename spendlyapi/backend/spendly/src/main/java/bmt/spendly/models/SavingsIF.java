package bmt.spendly.models;

public interface SavingsIF {
    Long getId();
    String getName();
    void setName(String name);
    double getAmount();
    void setAmount(double amount);
    AppUser getUser();
    void setUser(AppUser user);
}
