package bmt.spendly.models;

import java.util.List;

public interface GroupIF {
    Long getId();
    void setId(Long id);
    String getNome();
    void setNome(String nome);
    List<AppUser> getMembri();
    AppUser getAdmin();
    void setAdmin(AppUser admin);
    List<Alert> getAlerts();
    void AggiungiMembro(AppUser utente);
    void RimuoviMembro(AppUser utente);
    boolean ContieneMembro(AppUser utente);
    void addAlert(Alert alert);
    void removeAlert(Alert alert);
    List<String> getUsernames();
}
