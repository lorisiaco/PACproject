package com.bmt.spendly.models;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Spese") 
public class Cost {
    
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long CostId;

    private double importo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;  // Changed from Date to LocalDate

    private String tipologia;   //Per ora metto string, magari poi fare Enum

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false) // Chiave esterna per il gruppo
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Chiave esterna per l'utente
    private AppUser user;

    public Long getCostId() {
        return CostId;
    }

    public void setCostId(Long costId) {
        CostId = costId;
    }

    public double getImporto() {
        return importo;
    }

    public void setImporto(double importo) {
        this.importo = importo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    

}
