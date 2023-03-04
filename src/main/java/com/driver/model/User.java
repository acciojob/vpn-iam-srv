package com.driver.model;



import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class  User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String Username;

    private String password;

    private String OriginalIp;

    private String MaskedIp;

    private boolean connected;


    @ManyToMany
    @JoinColumn
    private List<ServiceProvider> serviceProviderList = new ArrayList<>();


    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    private List<Connection> connectionList = new ArrayList<>();

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL)
    private Country OriginalCountry;

    public User() {
    }

    public boolean isConnected() {
        return connected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOriginalIp() {
        return OriginalIp;
    }

    public void setOriginalIp(String originalIp) {
        this.OriginalIp = originalIp;
    }

    public String getMaskedIp() {
        return MaskedIp;
    }

    public void setMaskedIp(String maskedIp) {
        this.MaskedIp = maskedIp;
    }

    public boolean getConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public List<ServiceProvider> getServiceProviderList() {
        return serviceProviderList;
    }

    public void setServiceProviderList(List<ServiceProvider> serviceProviderList) {
        this.serviceProviderList = serviceProviderList;
    }

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }

    public Country getOriginalCountry() {
        return OriginalCountry;
    }

    public void setOriginalCountry(Country originalCountry) {
        this.OriginalCountry = originalCountry;
    }
}
