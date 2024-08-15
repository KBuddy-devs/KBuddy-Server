package com.example.kbuddy_backend.user.entity;

import com.example.kbuddy_backend.common.entity.BaseTimeEntity;
import com.example.kbuddy_backend.qna.entity.QnaHeart;
import com.example.kbuddy_backend.user.constant.Country;
import com.example.kbuddy_backend.user.constant.Gender;
import com.example.kbuddy_backend.user.constant.OAuthCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(columnDefinition = "BINARY(16)")
    private UUID uuid = UUID.randomUUID();

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Country country;

    private String bio;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserImage imageUrls;

    private boolean isActive = true;
    private LocalDateTime deactivationDate;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Authority> authorities = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Nullable
    private OAuthCategory oauthCategory;

    @OneToMany(mappedBy = "user")
    private List<QnaHeart> qnaHeart = new ArrayList<>();

    public void addAuthority(Authority authority) {
        authorities.add(authority);
        authority.setUser(this);
    }

    public void updateProfile(String userName, String email, Country country,Gender gender,String bio,String firstName,String lastName) {
        this.username = userName;
        this.email = email;
        this.country = country;
        this.gender = gender;
        this.bio = bio;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void resign() {
        this.isActive = false;
        this.deactivationDate = LocalDateTime.now();
    }

    public void assign() {
        this.isActive = true;
        this.deactivationDate = null;
    }

    public void setImageUrls(UserImage image) {
        this.imageUrls = image;
    }

    public void resetPassword(String password) {
        this.password = password;
    }

    @Builder
    public User(String username, String password, String email, String firstName, String lastName, Gender gender,
                Country country, String bio,
                @Nullable OAuthCategory oAuthCategory) {
        this.gender = gender;
        this.country = country;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.oauthCategory = oAuthCategory;
    }
}
