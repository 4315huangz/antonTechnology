package org.antontech.service;

import org.antontech.dto.ProjectDTO;
import org.antontech.model.Project;
import org.antontech.model.User;
import org.antontech.repository.Exception.UserNotFoundException;
import org.antontech.repository.IProjectDao;
import org.antontech.repository.IUserDao;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class ProjectService {
    @Autowired
    private IProjectDao projectDao;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private EmailService emailService;

    public List<ProjectDTO> getProjects() {
        List<Project> projects = projectDao.getProjects();
        return projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(project.getProjectId());
        projectDTO.setStartDate(project.getStartDate());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setManager(project.getManager());
        projectDTO.setUserIds(project.getUsers().stream()
                .map(User::getUserId)
                .collect(Collectors.toList()));
        return projectDTO;
    }

    public boolean save(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setStartDate(projectDTO.getStartDate());
        project.setDescription(projectDTO.getDescription());
        project.setManager(projectDTO.getManager());

        List<User> users = new ArrayList<>();
        for(long userId : projectDTO.getUserIds()) {
            User user = userDao.getById(userId);
            if(user == null)
                throw new IllegalArgumentException("User not found for ID: " + userId);
            users.add(user);
        }
        project.setUsers(users);
        Hibernate.initialize(project.getUsers());

        for(User user : project.getUsers()) {
            String userEmail = user.getEmail();
            String subject = "Project Created Notification";
            String body = "Dear " + user.getFirstName() + " " + user.getLastName() + ",\n\nThe project with ID "
                    + project.getProjectId() + " has been successfully created.";
            emailService.sendEmail(userEmail, subject, body);
        }
        return projectDao.save(project);
    }

    public ProjectDTO getById(long id) {
        Project project = projectDao.getById(id);
        if (project == null) {
            return null;
        }
        return convertToDTO(project);
    }

    public void updateDescription(long id, String description) {
        projectDao.updateDescription(id, description);
    }

    public void updateManager(long id, String manager) {
        projectDao.updateManager(id, manager);
    }

    public void delete(long id) {
        projectDao.delete(id);
    }
}

