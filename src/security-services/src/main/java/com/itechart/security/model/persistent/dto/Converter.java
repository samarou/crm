package com.itechart.security.model.persistent.dto;

import com.itechart.security.model.persistent.User;

/**
 * @author yauheni.putsykovich
 */
public class Converter {

    public static PublicUserDto convert(User user) {
        PublicUserDto publicUserDto = new PublicUserDto();
        publicUserDto.setId(user.getId());
        publicUserDto.setUserName(user.getUserName());
        publicUserDto.setEmail(user.getEmail());
        publicUserDto.setFirstName(user.getFirstName());
        publicUserDto.setLastName(user.getLastName());
        return publicUserDto;
    }

}
