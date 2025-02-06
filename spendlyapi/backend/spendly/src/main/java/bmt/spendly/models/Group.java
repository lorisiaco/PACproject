package bmt.spendly.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Gruppi")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "group_members",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnoreProperties({"groups"}) 
    // Se in AppUser hai una relazione inversa con Group (ad es. `private List<Group> groups;`),
    // questa annotazione evita cicli di serializzazione
    private List<AppUser> membri = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private AppUser admin;

    // Costruttori
    public Group() {}

    public Group(String nome, AppUser admin) {
        this.nome = nome;
        this.admin = admin;
    }

    public Group(String nome) {
        this.nome = nome;
    }

    // Getter & Setter
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }
    public void setNome(String s) {
        this.nome = s;
    }

    public List<AppUser> getMembri() {
        return this.membri;
    }

    public AppUser getAdmin() {
        return this.admin;
    }
    public void setAdmin(AppUser admin) {
        this.admin = admin;
    }

    // Metodi di utilità
    public void AggiungiMembro(AppUser utente) {
        if (!membri.contains(utente)) {
            membri.add(utente);
        } else {
            throw new IllegalArgumentException("L'utente è già nel gruppo");
        }
    }
    
    public void RimuoviMembro(AppUser utente) {
        if (membri.contains(utente)) {
            membri.remove(utente);
        } else {
            throw new IllegalArgumentException("L'utente non è nel gruppo");
        }
    }

    public boolean ContieneMembro(AppUser utente) {
        return membri.contains(utente);
    }
}
