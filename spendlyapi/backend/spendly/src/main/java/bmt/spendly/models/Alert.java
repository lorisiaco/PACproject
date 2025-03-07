package bmt.spendly.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Alert")
public class Alert implements AlertIF {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double limite;
    private String nome;
    
    @Enumerated(EnumType.STRING)
    private ExpenseMacroArea macroArea;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Alert() {}

    public Alert(String nome, double limite, ExpenseMacroArea macroArea, Group group) {
        this.nome = nome;
        this.limite = limite;
        this.macroArea = macroArea;
        this.group = group;
    }

    @Override
    public Long getId() {
        return this.id;
    }
    
    @Override
    public double getLimite() {
        return this.limite;
    }

    @Override
    public void setLimite(double limite) {
        this.limite = limite;
    }

    @Override
    public String getNome() {
        return this.nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public ExpenseMacroArea getMacroArea() {
        return this.macroArea;
    }

    @Override
    public void setMacroArea(ExpenseMacroArea macroArea) {
        this.macroArea = macroArea;
    }

    @Override
    public Group getGroup() {
        return this.group;
    }

    @Override
    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Alert{id=" + id + ", nome='" + nome + "', limite=" + limite 
            + ", macroArea=" + macroArea 
            + ", groupId=" + (group != null ? group.getId() : null) + "}";
    }
}
