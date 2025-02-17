package bmt.spendly.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
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
@Table(name = "Spese") 
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cost {
    
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long CostId;

    private double importo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipologia", nullable = false)
    private ExpenseType tipologia;   //Per ora metto string, magari poi fare Enum

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = true)

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

    public ExpenseType getTipologia() {
        return tipologia;
    }

    public void setTipologia(ExpenseType tipologia) {
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

enum ExpenseType {
    // Spese Essenziali
    ABITAZIONE_AFFITTO,
    ABITAZIONE_MUTUO,
    ABITAZIONE_BOLLETTE,
    ALIMENTARI,
    TRASPORTI_CARBURANTE,
    TRASPORTI_PUBBLICO,
    TRASPORTI_MANUTENZIONE,
    TRASPORTI_ASSICURAZIONE,
    SALUTE_FARMACI,
    SALUTE_VISITE,
    SALUTE_ASSICURAZIONE,
    ISTRUZIONE_TASSE,
    ISTRUZIONE_MATERIALI,
    ISTRUZIONE_CORSI,
    ASSICURAZIONI_AUTO,
    ASSICURAZIONI_CASA,
    ASSICURAZIONI_VITA,
    TASSE_PROPRIETA,
    
    // Spese Discrezionali
    SVAGO_CINEMA,
    SVAGO_TEATRO,
    SVAGO_CONCERTI,
    SVAGO_HOBBY,
    VIAGGI_BIGLIETTI,
    VIAGGI_HOTEL,
    VIAGGI_ESCURSIONI,
    RISTORANTI_PRANZI,
    RISTORANTI_CENE,
    RISTORANTI_CAFFE,
    SHOPPING_ABBIGLIAMENTO,
    SHOPPING_ACCESSORI,
    SHOPPING_SCARPE,
    SHOPPING_COSMETICI,
    TECNOLOGIA_SMARTPHONE,
    TECNOLOGIA_TABLET,
    TECNOLOGIA_COMPUTER,
    TECNOLOGIA_ABBONAMENTI
}