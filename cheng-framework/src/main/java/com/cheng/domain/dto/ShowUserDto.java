package com.cheng.domain.dto;

import com.cheng.domain.entity.Role;
import com.cheng.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowUserDto {

    private List<Long> roleIds;

    private List<Role> roles;

    private User user;

}
