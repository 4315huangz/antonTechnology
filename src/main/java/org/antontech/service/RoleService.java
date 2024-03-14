package org.antontech.service;

import org.antontech.model.Role;
import org.antontech.repository.IRoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    IRoleDao roleDao;

    public List<Role> getRoles() {
        return roleDao.getRoles();
    }

    public Role getById(long id) {
        return roleDao.getById(id);
    }

    public boolean save(Role role) {
        return roleDao.save(role);
    }

    public void delete(Role role) {
        roleDao.delete(role);
    }

    public String getAllowedReadResources(Role role){
        return roleDao.getAllowedReadResources(role);
    }

    public String getAllowedCreateResources(Role role){ return roleDao.getAllowedCreateResources(role);}

    public String getAllowedUpdateResources(Role role){ return roleDao.getAllowedUpdateResources(role);}

    public String getAllowedDeleteResources(Role role){ return roleDao.getAllowedDeleteResources(role);}

}
