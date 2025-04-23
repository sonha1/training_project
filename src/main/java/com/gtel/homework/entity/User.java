package com.gtel.homework.entity;

import com.gtel.homework.entity.converters.UserStatusConverter;
import com.gtel.homework.enums.UserRole;
import com.gtel.homework.model.request.RegisterRequest;
import com.gtel.homework.utils.BcryptUtils;
import com.gtel.homework.utils.USER_STATUS;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Table(name = "users")
@Entity
@NoArgsConstructor
public class User extends BaseEntity {
    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "status")
    private Integer status;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "country")
    private String country;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User(RegisterRequest request) {
        this.username = request.getUsername();
        this.email = request.getEmail();
        this.fullName = request.getFullName();
        this.dateOfBirth = request.getDateOfBirth();
        this.gender = request.getGender();
        this.status = request.getStatus();
        this.phoneNumber = request.getPhoneNumber();
        this.password = BcryptUtils.encode(request.getPassword());
        this.country = request.getCountry();
        this.avatarUrl = request.getAvatarUrl();
    }
}
