package com.bookstore.BookStore.models;
import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "Gruppi")
public class Group {

     /**
     * Id of the Group
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Group Name
     */
    private String nome;

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


    /**
     * Constructors
     */
    public Group(){}

    public Group(String n){
        super();
        this.nome=n;
    }

    /**
     * 
     * @return return the Id of the Group
     */
    public  Long getId(){
        return this.id;
    }

    /**
     * 
     * @return return the name of the Group
     */

    public String getNome(){
        return this.nome;
    }

    /**
     * 
     * @return return the Members of the Group
     */

    public List<AppUser> getMembri(){   // la lista non è immutabile come nome e id, dovrei stare attento a ritornare la lista vera
        return this.membri;
    }


    /**
     * Adding new member
     * @param utente
     */
    public void AggiungiMembro(AppUser utente){
        if(!membri.contains(utente)){
            membri.add(utente);
        } else {
            throw new IllegalArgumentException("L'utente è già nel gruppo");
        }
    }
    /**
     * Removing member method
     * @param utente
     */

    public void RimuoviMembro(AppUser utente){
        if(membri.contains(utente)){
            membri.remove(utente);
        } else {
            throw new IllegalArgumentException ("L'utente non è nel gruppo");
        }
    }

    /**
     * 
     * @param utente
     * @return a boolean to check if an user is a member of the group
     */

    public boolean ContieneMembro(AppUser utente){
        return membri.contains(utente);
    }

    
}
