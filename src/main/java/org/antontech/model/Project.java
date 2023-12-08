package org.antontech.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="project_id")
    private int project_id;
    @Column(name = "oem")
    private int oem;
    @Column(name = "supplier")
    private int supplier;
    @Column(name = "start_date")
    private Date start_date;
    @Column(name = "description")
    private String description;
    @Column(name = "manager")
    private String manager;

    public Project() {
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getOem() {return oem;}

    public void setOem(int oem) {this.oem = oem;}

    public int getSupplier() {return supplier;}

    public void setSupplier(int supplier) {this.supplier = supplier;}

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
}
