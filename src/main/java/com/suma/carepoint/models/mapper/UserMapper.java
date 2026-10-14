package com.suma.carepoint.models.mapper;

import com.suma.carepoint.entities.auth.User;
import com.suma.carepoint.models.auth.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserResponse toResponse(User user) {

        UserResponse response = modelMapper.map(user, UserResponse.class);
        if (user.getStaff() != null) {
            response.setStaffId(user.getStaff().getStaffId());
            response.setEmployeeNo(user.getStaff().getEmployeeNo());
            response.setFirstName(user.getStaff().getFirstName());
            response.setLastName(user.getStaff().getLastName());
        }
        return response;
    }
}
