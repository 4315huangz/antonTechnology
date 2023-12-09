package org.antontech.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="project_id")
    private long project_id;
    @Column(name = "oem")
    private long oem;
    @Column(name = "supplier")
    private long supplier;
    @Column(name = "start_date", columnDefinition = "DATE")
    private Date start_date;
    @Column(name = "description")
    private String description;
    @Column(name = "manager")
    private String manager;

    public Project() {
    }

    public long getProject_id() {
        return project_id;
    }

    public void setProject_id(long project_id) {
        this.project_id = project_id;
    }

    public long getOem() {return oem;}

    public void setOem(long oem) {this.oem = oem;}

    public long getSupplier() {return supplier;}

    public void setSupplier(long supplier) {this.supplier = supplier;}

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
