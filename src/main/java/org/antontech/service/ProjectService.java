package org.antontech.service;

import org.antontech.model.Project;
import org.antontech.model.User;
import org.antontech.repository.IProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProjectService {
    @Autowired
    private IProjectDao projectDao;

    public List<Project> getProjects(User user) {
        return projectDao.getProjects();
    }

    public boolean save(Project project) {
        return projectDao.save(project);
    }

    public Project getById(long id) {
        return projectDao.getById(id);
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

