package bmt.spendly.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "savings")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Savings implements SavingsIF {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double amount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    public Savings() {}

    public Savings(AppUser user, String name, double amount) {
        this.user = user;
        this.name = name;
        this.amount = amount;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public AppUser getUser() {
        return user;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public void setUser(AppUser user) {
        this.user = user;
    }
}
