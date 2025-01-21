package spendly.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Gruppi") 
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private List<UserApp> membri=new ArrayList<UserApp>();

    public Group(){}

    public Group(String n){
        super();
        this.nome=n;
    }

    public void AggiungiMembro(UserApp utente){
        if(!membri.contains(utente)){
            membri.add(utente);
        } else {
            throw new IllegalArgumentException("L'utente è già nel gruppo");
        }
    }

    public void RimuoviMembro(UserApp utente){
        if(membri.contains(utente)){
            membri.remove(utente);
        } else {
            throw new IllegalArgumentException ("L'utente non è nel gruppo");
        }
    }

    public boolean ContieneMembro(UserApp utente){
        return membri.contains(utente);
    }

    public void VisualizzaMembri(){
        Iterator<UserApp> it=membri.iterator();
        while(it.hasNext()){
            System.out.println(it.next().toString());
            System.out.println("--");
        }
    }

    

}
