package org.antontech.repository;

import org.antontech.model.Role;

import java.util.List;

public interface IRoleDao {
    List<Role> getRoles();

    Role getById(long id);

    boolean save(Role role);

    void delete (Role role);

    String getAllowedResources(Role role);

    String getAllowedReadResources(Role role);

    String getAllowedCreateResources(Role role);

    String getAllowedUpdateResources(Role role);

    String getAllowedDeleteResources(Role role);

}
