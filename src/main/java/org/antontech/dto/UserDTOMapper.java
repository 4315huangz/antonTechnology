package org.antontech.dto;

import org.antontech.model.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail(),
                user.getCompanyName(),
                user.getIndustry(),
                user.getTitle(),
                user.getCompanyType()
        );
    }
}
