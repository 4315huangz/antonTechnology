package org.antontech.service;

import org.antontech.controller.ProjectController;
import org.antontech.dto.ProjectDTO;
import org.antontech.dto.ProjectDTOMapper;
import org.antontech.model.Project;
import org.antontech.model.User;
import org.antontech.repository.IProjectDao;
import org.antontech.repository.IUserDao;
import org.antontech.service.exception.ResourceNotFoundException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ProjectService {
    @Autowired
    private IProjectDao projectDao;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ProjectDTOMapper projectDTOMapper;
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);


    public Set<ProjectDTO> getProjects() {
        logger.debug("Get projects in service layer");
        return projectDao.getProjects()
                .stream()
                .map(projectDTOMapper)
                .collect(Collectors.toSet());
    }


    public boolean save(ProjectDTO projectDTO) {
        logger.debug("Start save project at service layer.");
        Project project = new Project();
        project.setStartDate(projectDTO.getStartDate());
        project.setDescription(projectDTO.getDescription());
        project.setManager(projectDTO.getManager());

        List<User> users = new ArrayList<>();
        logger.debug("Save this project to each associated user.");
        for(long userId : projectDTO.getParticipateId()) {
            User user = userDao.getById(userId);
            if(user == null)
                throw new ResourceNotFoundException("User not found for ID: " + userId);
            user.getProjects().add(project);
            userDao.save(user);
            users.add(user);
        }
        logger.debug("Save the participated users for created project.");
        project.setUsers(users);
        Hibernate.initialize(project.getUsers());

        logger.debug("Send the notification to participants of saved project.");
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
        logger.debug("Retrieve the project DTO by project ID through service layer.");
        Project project = projectDao.getById(id);
        if (project != null) {
            return projectDTOMapper.apply(project);
        } else
            throw new ResourceNotFoundException("Project with id " + id + " not found");
    }

    public void updateDescription(long id, String description) {
        projectDao.updateDescription(id, description);
    }

    public void updateManager(long id, String manager) {
        projectDao.updateManager(id, manager);
    }

    public void addProjectParticipates(long projectId, long userId) {
        logger.debug("Update participates of existing project at service layer");
        User user = userDao.getById(userId);
        if(user == null)
            throw new ResourceNotFoundException("User not found for ID: " + userId);

        Project project = projectDao.getById(projectId);
        if(project == null)
            throw new ResourceNotFoundException("Project not found for ID: " + projectId);

        logger.debug("Add the user to project id = {}", projectId);
        if (!userAlreadyParticipates(projectId, userId)) {
            project.getUsers().add(user);
            projectDao.save(project);

            logger.debug("Send notification to newly added participant");
            String userEmail = user.getEmail();
            String subject = "You've been added to a project";
            String body = "Dear " + user.getFirstName() + " " + user.getLastName() + ",\n\nYou have been added to the project with ID "
                    + project.getProjectId() + ".";
            emailService.sendEmail(userEmail, subject, body);
        } else {
            logger.debug("User with ID {} already exists in project with ID {}", userId, projectId);
        }

        logger.debug("Add the project to user id = {}", userId);
        if (!projectAlreadyAddedToUser(userId, projectId)) {
            user.getProjects().add(project);
            userDao.save(user);
        } else {
            logger.debug("Project with ID {} already exists for user with ID {}", projectId, userId);
        }
    }

    public void deleteProjectParticipates(long projectId, long userId) {
        logger.debug("Remove user from project at service layer");
        User user = userDao.getById(userId);
        if(user == null)
            throw new ResourceNotFoundException("User not found for ID: " + userId);

        Project project = projectDao.getById(projectId);
        if(project == null)
            throw new ResourceNotFoundException("Project not found for ID: " + projectId);

        logger.debug("Remove user from project id = {}", projectId);
        if (userAlreadyParticipates(projectId, userId)) {
            List<User> users = project.getUsers();
            users.removeIf(u -> u.getUserId() == userId);
            projectDao.save(project);

            logger.debug("Send notification to removed participant");
            String userEmail = user.getEmail();
            String subject = "You've been removed from a project";
            String body = "Dear " + user.getFirstName() + " " + user.getLastName() + ",\n\nYou have been removed from the project with ID "
                    + project.getProjectId() + ".";
            emailService.sendEmail(userEmail, subject, body);
        } else {
            logger.debug("User with ID {} does not exist in project with ID {}", userId, projectId);
        }

        logger.debug("Remove project from user id = {}", userId);
        if (projectAlreadyAddedToUser(userId, projectId)) {
            List<Project> projects = user.getProjects();
            projects.removeIf(p -> p.getProjectId() == projectId);
            userDao.save(user);
        } else {
            logger.debug("Project with ID {} does not exist for user with ID {}", projectId, userId);
        }
    }

    public void delete(long id) {
        projectDao.delete(id);
    }

    private boolean userAlreadyParticipates(long projectId, long userId) {
        for (User user : projectDao.getById(projectId).getUsers()) {
            if (user.getUserId() == userId) {
                return true;
            }
        }
        return false;
    }

    private boolean projectAlreadyAddedToUser(long userId, long projectId) {
        for (Project userProject : userDao.getById(userId).getProjects()) {
            if (userProject.getProjectId() == projectId) {
                return true;
            }
        }
        return false;
    }
}

