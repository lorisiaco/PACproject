package bmt.spendly.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Rappresenta il "portafoglio" di un utente.
 */
@Entity
@Table(name = "Budget") 
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Budget {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Utente proprietario di questo budget
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private AppUser user;

    // Ammontare del budget (quanti soldi ha l'utente)
    private double amount;

    public Budget() {
    }

    public Budget(AppUser user, double amount) {
        this.user = user;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
