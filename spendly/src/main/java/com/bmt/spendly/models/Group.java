package com.bmt.spendly.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "Gruppi") 
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private List<AppUser> membri=new ArrayList<AppUser>();

    public Group(){}

    public Group(String n){
        super();
        this.nome=n;
    }

    public String getNome(){
        return this.nome;
    }
    public  Long getId(){
        return this.id;
    }

    public List<AppUser> getMembri(){   // la lista non è immutabile come nome e id, dovrei stare attento a ritornare la lista vera
        return this.membri;
    }

    public void AggiungiMembro(AppUser utente){
        if(!membri.contains(utente)){
            membri.add(utente);
        } else {
            throw new IllegalArgumentException("L'utente è già nel gruppo");
        }
    }

    public void RimuoviMembro(AppUser utente){
        if(membri.contains(utente)){
            membri.remove(utente);
        } else {
            throw new IllegalArgumentException ("L'utente non è nel gruppo");
        }
    }

    public boolean ContieneMembro(AppUser utente){
        return membri.contains(utente);
    }

    public void VisualizzaMembri() {
        if (membri.isEmpty()) {
            System.out.println("Nessun membro presente.");
            return;
        }
    
        for (AppUser membro : membri) {
            System.out.println(membro); // `toString` chiamato implicitamente
        }
    }



}
