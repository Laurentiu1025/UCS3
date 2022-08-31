package ro.itschool.controller.mapper;

import ro.itschool.controller.model.MyUserDTO;
import ro.itschool.entity.MyUser;
import ro.itschool.entity.Role;

public class MyUserMapper {

    public static MyUserDTO convertToDTO(MyUser myUser) {
        MyUserDTO myUserDTO = new MyUserDTO();
        myUserDTO.setId(myUser.getId());
        myUserDTO.setUsername(myUser.getUsername());
        myUserDTO.setEmail(myUser.getEmail());
        myUserDTO.setFullName(myUser.getFullName());

        myUserDTO.setRoles(myUser.getRoles().stream()
                .map(Role::getName)
                .toList());

        return myUserDTO;
    }

}
