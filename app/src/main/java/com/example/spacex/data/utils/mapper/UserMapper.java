package com.example.spacex.data.utils.mapper;

import androidx.annotation.Nullable;

import com.example.spacex.data.dto.UserDto;
import com.example.spacex.domain.entity.UserEntity;

public class UserMapper {

    @Nullable
    public static UserEntity toUserEntity(@Nullable UserDto userDto) {
        if (userDto == null) return null;

        final String id = userDto.id;
        final String phone = userDto.phone;
        final String name = userDto.name;
        final String username = userDto.username;
        final String email = userDto.email;
        final String photoUrl = userDto.photoUrl;

        if (id != null && name != null && username != null && email != null) {
            return new UserEntity(
                    id,
                    phone,
                    name,
                    username,
                    email,
                    photoUrl
            );
        }

        return null;
    }
}

