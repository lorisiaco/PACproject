package com.bmt.spendly.models;

import java.util.Date;

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