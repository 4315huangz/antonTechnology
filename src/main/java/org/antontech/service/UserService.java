package org.antontech.service;

import org.antontech.dto.UserDTO;
import org.antontech.dto.UserDTOMapper;
import org.antontech.model.User;
import org.antontech.repository.Exception.UserDaoException;
import org.antontech.repository.UserHibernateDao;
import org.antontech.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {
    @Autowired
    private UserHibernateDao userDao;
    @Autowired
    private UserDTOMapper userDTOMapper;

    public List<UserDTO> getUsers() {
        return userDao.getUsers()
                .stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());
    }

    public boolean save(User user) {
        return userDao.save(user);
    }

    public UserDTO getUserDTOById(long id) {
        User u = userDao.getById(id);
        if (u != null)
            return userDTOMapper.apply(u);
        else
            throw new ResourceNotFoundException("User with id " + id + " not found");
    }

    public User getUserSecurityById(long id) {
        return userDao.getById(id);
    }

    public List<UserDTO> getByIndustry(String industry) {
        return userDao.getByIndustry(industry)
                .stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());
    }

    public void updateEmail(long id, String email) {
        userDao.updateEmail(id, email);
    }

    public void updatePassword(long id, String password) {
        userDao.updatePassword(id, password);
    }

    public void updateCompany(long id, String companyName, String address, String industry) {
        userDao.updateCompany(id, companyName, address, industry);
    }

    public void updateManager(long id, String firstName, String lastName, String title, String phone) {
        userDao.updateManager(id, firstName, lastName, title, phone);
    }

    public void delete(long id) {
        userDao.delete(id);
    }

    public User getUserByCredentials(String email, String password) {
        try {
            return userDao.getUserByCredentials(email, password);
        } catch (UserDaoException e) {
            throw new ResourceNotFoundException("Failed to get user credentials.", e);
        }

    }
}