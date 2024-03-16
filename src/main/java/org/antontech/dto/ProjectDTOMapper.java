package org.antontech.dto;

import org.antontech.model.Project;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProjectDTOMapper implements Function<Project, ProjectDTO> {
    @Override
    public ProjectDTO apply(Project project) {
        return new ProjectDTO(
                project.getProjectId(),
                project.getStartDate(),
                project.getDescription(),
                project.getManager(),
                project.getUsers().stream().map(user -> user.getUserId()).collect(Collectors.toList()),
                project.getUsers().stream().map(user -> user.getCompanyName()).collect(Collectors.toList())
                );
    }

}
