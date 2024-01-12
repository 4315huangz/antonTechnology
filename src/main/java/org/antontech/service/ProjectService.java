package org.antontech.service;

import org.antontech.model.Product;
import org.antontech.model.Project;
import org.antontech.model.User;
import org.antontech.repository.IProjectDao;
import org.antontech.repository.IUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Autowired
    private IProjectDao projectDao;

    @Autowired
    private IUserDao userDao;

    public List<Project> getProjects(User user) {
        if("OEM".equals(user.getType())) {
            return projectDao.getProjectsByOEM(user.getUserId());
        } else {
            return projectDao.getProjectsBySupplier(user.getUserId());
        }
    }

    public boolean save(Project project) {
        return projectDao.save(project);
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
