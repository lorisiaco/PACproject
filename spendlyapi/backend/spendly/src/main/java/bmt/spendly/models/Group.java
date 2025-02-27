package bmt.spendly.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Gruppi")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Group implements GroupIF {

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
    
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL,fetch = FetchType.EAGER, orphanRemoval = true )
    @JsonIgnoreProperties("group") // Evita cicli infiniti nella serializzazione JSON
    private List<Alert> alerts = new ArrayList<>();

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
    @Override
    public Long getId() {
        return this.id;
    }
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getNome() {
        return this.nome;
    }
    @Override
    public void setNome(String s) {
        this.nome = s;
    }

    @Override
    public List<AppUser> getMembri() {
        return this.membri;
    }

    @Override
    public AppUser getAdmin() {
        return this.admin;
    }
    @Override
    public void setAdmin(AppUser admin) {
        this.admin = admin;
    }

    @Override
    public List<Alert> getAlerts(){
        return this.alerts;
    }

    // Metodi di utilità
    @Override
    public void AggiungiMembro(AppUser utente) {
        if (!membri.contains(utente)) {
            membri.add(utente);
        } else {
            throw new IllegalArgumentException("L'utente è già nel gruppo");
        }
    }
    
    @Override
    public void RimuoviMembro(AppUser utente) {
        if (membri.contains(utente)) {
            membri.remove(utente);
        } else {
            throw new IllegalArgumentException("L'utente non è nel gruppo");
        }
    }

    @Override
    public boolean ContieneMembro(AppUser utente) {
        return membri.contains(utente);
    }

    @Override
    public void addAlert(Alert alert) {
        this.alerts.add(alert);
    }
    
    @Override
    public void removeAlert(Alert alert) {
        this.alerts.remove(alert);
    }

    @Override
    public List<String> getUsernames() {
        List<String> usernames = new ArrayList<>();
        for (AppUser user : membri) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }
}
