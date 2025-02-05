package com.bookstore.BookStore.models;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
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
public class Group {

     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // MODIFICATO da int a Long
    private String nome;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "group_members",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )

    /**
     * Members of the Group
     */
    private List<AppUser> membri=new ArrayList<AppUser>();

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private AppUser admin;

    //costruttori
    public Group() {}

    public Group(String nome, AppUser admin) {
        this.nome = nome;
        this.admin = admin;
    }
    public Group(String nome) {
        this.nome = nome;
        
    }
    

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getNome(){
        return this.nome;
    }
    
    public void setNome(String s){
        this.nome=s;
    }

    public List<AppUser> getMembri(){  
        return this.membri;
    }

    public AppUser getAdmin(){
        return this.admin;
    }

    public void setAdmin(AppUser Admin){
        this.admin=Admin;
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

    
}
