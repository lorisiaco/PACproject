package bmt.spendly.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    // Riferimento al gruppo di spesa (assicurati che la classe Group sia opportunamente definita)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
    
    // L'utente che ha creato la spesa (assicurati che la classe User sia opportunamente definita e implementi UserDetails, se usata in Spring Security)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private AppUser createdBy;
    
    
    // Se la spesa è individuale, qui viene indicato il beneficiario (facoltativo per spese di gruppo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiary_id")
    private AppUser beneficiary;

    // Costruttore di default
    public Expense() {
    }

    // Costruttore completo
    public Expense(String description, BigDecimal amount, Group group, AppUser createdBy, ExpenseType expenseType, AppUser beneficiary) {
        this.description = description;
        this.amount = amount;
        this.group = group;
        this.createdBy = createdBy;
        
        this.beneficiary = beneficiary;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUser createdBy) {
        this.createdBy = createdBy;
    }

    

    public AppUser getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(AppUser beneficiary) {
        this.beneficiary = beneficiary;
    }
}



