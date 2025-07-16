package com.project.shopapp.models;

import com.project.shopapp.helpers.models.BaseModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "users")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "phone_number",nullable = false,length = 10)
    private String phoneNumber;

    @Column(name = "address",length = 200)
    private String address;

    @Column(name = "password",length = 200,nullable = false)
    private String password;

    @Column(name = "is_active")
    private Boolean active;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "facebook_id")
    private Long facebookId;

    @Column(name = "google_id")
    private Long googleId;


    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
