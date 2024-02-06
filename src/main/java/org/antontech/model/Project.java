package org.antontech.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="project_id")
    private long projectId;
    @Column(name = "start_date", columnDefinition = "DATE")
    private Date startDate;
    @Column(name = "description")
    private String description;
    @Column(name = "manager")
    private String manager;

    @ManyToMany(mappedBy = "projects", fetch = FetchType.EAGER)
    private List<User> users;

    public Project() { }

    public Project(long id, Date startDate, String description, String manager) { }

    public long getProjectId() {return projectId;}

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
