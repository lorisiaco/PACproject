package bmt.spendly.models;

import java.util.Date;

public interface AppUserIF {
    int getId();
    void setId(int id);
    String getFirstName();
    void setFirstName(String firstName);
    String getLastName();
    void setLastName(String lastName);
    String getUsername();
    void setUsername(String username);
    String getEmail();
    void setEmail(String email);
    String getPhone();
    void setPhone(String phone);
    String getAddress();
    void setAddress(String address);
    String getPassword();
    void setPassword(String password);
    String getRole();
    void setRole(String role);
    Date getCreatedAt();
    void setCreatedAt(Date createdAt);
}
