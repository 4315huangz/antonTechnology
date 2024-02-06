package org.antontech.controller;

import org.antontech.dto.ProjectDTO;
import org.antontech.model.Project;
import org.antontech.service.ProjectService;
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
import java.util.List;

@RestController
@RequestMapping(value = {"/project","/projects"})
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    @Autowired
    private ProjectService projectService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<ProjectDTO>> getProjects(){
        logger.info("I am in getProejcts controller");
        List<ProjectDTO> projects = projectService.getProjects();
        if(projects == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(projects);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String create(@RequestBody ProjectDTO projectDTO) {
        logger.info("Post a new project object {}", projectDTO.getProjectId());
        if(projectService.save(projectDTO)) {
            return "Project created successfully";
        }
        return null;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity getProjectById(@PathVariable(name = "id")long id){
        ProjectDTO p = projectService.getById(id);
        if(p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project with ID " + id + " is not found");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(p);
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
