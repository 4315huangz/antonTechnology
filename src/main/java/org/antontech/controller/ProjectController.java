package org.antontech.controller;

import org.antontech.dto.ProjectDTO;
import org.antontech.dto.UserDTO;
import org.antontech.service.ProjectService;
import org.antontech.service.UserService;
import org.antontech.service.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Set;

@RestController
@RequestMapping(value = {"/project","/projects"})
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Set<ProjectDTO>> getProjects(){
        logger.debug("I am in get Projects controller");
        Set<ProjectDTO> projects = projectService.getProjects();
        if(projects == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptySet());
        }
        return ResponseEntity.ok(projects);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<String> create(@RequestBody ProjectDTO projectDTO) {
        logger.info("Post a new project object {}", projectDTO.getProjectId());
        if(projectService.save(projectDTO)) {
            return ResponseEntity.ok("Project created successfully");
        }
        return ResponseEntity.badRequest().body("Failed to create project");
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity getProjectById(@PathVariable(name = "id")long id){
        logger.info("Search project in project controller");
        try {
            ProjectDTO p = projectService.getById(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(p);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project with ID " + id + " is not found");
        }
    }

    @RequestMapping(value = "/addMember/{projectId}/{userId}", method = RequestMethod.PATCH)
    public ResponseEntity addProjectParticipates(@PathVariable(name = "projectId") long projectId, @PathVariable (name = "userId") long userId) {
        logger.info("Add team member id = {} to project id = {}", userId, projectId);
        if (getProjectById(projectId).getStatusCode() == HttpStatus.ACCEPTED) {
            if (getUserById(userId).getStatusCode() == HttpStatus.ACCEPTED) {
                projectService.addProjectParticipates(projectId, userId);
                return ResponseEntity.ok().body("User id = " + userId + " is added to project id " + projectId + " successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + userId + " not found");
            }
        } else
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project with ID " + projectId + " is not found, fail to add team member");
    }

    @RequestMapping(value = "/removeMember/{projectId}/{userId}", method = RequestMethod.PATCH)
    public ResponseEntity removeProjectParticipates(@PathVariable(name = "projectId") long projectId, @PathVariable (name = "userId") long userId) {
        logger.info("Remove team member id = {} from project id = {}", userId, projectId);
        if (getProjectById(projectId).getStatusCode() == HttpStatus.ACCEPTED) {
            if (getUserById(userId).getStatusCode() == HttpStatus.ACCEPTED) {
                projectService.deleteProjectParticipates(projectId, userId);
                return ResponseEntity.ok().body("User id = " + userId + " is removed from project id " + projectId + " successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + userId + " not found");
            }
        } else
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project with ID " + projectId + " is not found, fail to remove team member");
    }

    private ResponseEntity<UserDTO> getUserById(long id) {
        try {
            UserDTO u = userService.getUserDTOById(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(u);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @RequestMapping(value = "/changeDescription/{id}", params = {"description"}, method = RequestMethod.PATCH)
    public ResponseEntity<String> updateProjectDescription(@PathVariable(name = "id") long id, @RequestParam(name = "description") String description){
        logger.info("Pass in variable id: {} and description {}.", id, description);
        if(getProjectById(id).getStatusCode() == HttpStatus.ACCEPTED) {
            projectService.updateDescription(id,description);
            return ResponseEntity.ok().body("Description for project "+id+" is updated successfully");
        } else
            return  ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Project with ID " + id + " is not found, fail to update the description");
    }

    @RequestMapping(value = "/changeManager/{id}", params = {"manager"}, method = RequestMethod.PATCH)
    public ResponseEntity<String> updateProjectManager(@PathVariable(name = "id") long id, @RequestParam(name = "manager") String manager){
        logger.info("Pass in variable id: {} and manager {}.", id, manager);
        if(getProjectById(id).getStatusCode() == HttpStatus.ACCEPTED) {
            projectService.updateManager(id, manager);
            return ResponseEntity.ok().body("Manager for project " + id + " is updated successfully");
        } else return  ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Project with ID " + id + " is not found, fail to update the manager");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteProject(@PathVariable long id) {
        logger.info("I am in delete Project controller");
        if(getProjectById(id).getStatusCode() == HttpStatus.ACCEPTED) {
            projectService.delete(id);
            return ResponseEntity.ok().body("Project " + id + " is deleted successfully");
        } else return  ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Project with ID " + id + " is not found, fail to delete");
    }

}
