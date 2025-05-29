package com.example.spacex.data.dto;

public class UpdatedUserDto {

    private final String id;

    private final String name;

    private final String username;

    private final String photoUrl;

    private final String phone;

    private final String email;

    public UpdatedUserDto(
            String id, String name,
            String username,
            String photoUrl,
            String phone,
            String email
    ) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.photoUrl = photoUrl;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }
}
