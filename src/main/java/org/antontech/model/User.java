package org.antontech.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int user_id;
    @Column(name = "company_name")
    private String company_name;
    @Column(name = "address")
    private String address;
    @Column(name = "industry")
    private String industry;
    @Column(name = "manager_name")
    private String manager_name;
    @Column(name = "title")
    private String title;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "type")
    private String type;
    @Column(name = "product_id")
    private int product_id;

    public User() {
    }

    public int getUser_id() {return user_id;}

    public void setUser_id(int user_id) {this.user_id = user_id;}

    public String getCompany_name() {return company_name;}

    public void setCompany_name(String company_name) {this.company_name = company_name;}

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}

    public String getIndustry() {return industry;}

    public void setIndustry(String industry) {this.industry = industry;}

    public String getManager_name() {return manager_name;}

    public void setManager_name(String manager_name) {this.manager_name = manager_name;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    public int getProduct_id() {return product_id;}

    public void setProduct_id(int product_id) {this.product_id = product_id;}
}

