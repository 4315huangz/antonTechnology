package org.antontech.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import java.util.Date;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="project_id")
    private long projectId;
    @Column(name = "oem")
    private long oem;
    @Column(name = "supplier")
    private long supplier;
    @Column(name = "start_date", columnDefinition = "DATE")
    private Date startDate;
    @Column(name = "description")
    private String description;
    @Column(name = "manager")
    private String manager;

    public Project() {
    }

    public long getProjectId() {return projectId;}

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getOem() {return oem;}

    public void setOem(long oem) {this.oem = oem;}

    public long getSupplier() {return supplier;}

    public void setSupplier(long supplier) {this.supplier = supplier;}

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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
