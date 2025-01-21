package spendly.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Utenti") 
public class UserApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognonome;

    @Column(unique = true, nullable = false)
    private String email;
    
    private String telefono;
    private String password;

    public UserApp(){}

    public UserApp(String n,String e){
        super();
        this.nome=n;
        this.email=e;
    }
    
     // Getter e Setter
     public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognonome() {
        return cognonome;
    }

    public void setCognonome(String cognonome) {
        this.cognonome = cognonome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public  String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString(){
        return "Id="+ this.getId()+ "Nome="+this.getNome()+"Cognome"+this.getCognonome();
    }

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserApp userApp = (UserApp) o;
        return id != null && id.equals(userApp.id);
    }
}
