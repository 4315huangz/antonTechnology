package org.antontech.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "address")
    private String address;
    @Column(name = "industry")
    private String industry;
    @Column(name = "manager_name")
    private String managerName;
    @Column(name = "title")
    private String title;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "type")
    private String type;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Product> products;

    public User() {
    }

    public long getUserId() {return userId;}

    public void setUserId(long userId) {this.userId = userId;}

    public String getCompanyName() {return companyName;}

    public void setCompanyName(String companyName) {this.companyName = companyName;}

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}

    public String getIndustry() {return industry;}

    public void setIndustry(String industry) {this.industry = industry;}

    public String getManagerName() {return managerName;}

    public void setManagerName(String managerName) {this.managerName = managerName;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}


}

