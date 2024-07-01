package com.example.kbuddy_backend.user.entity;

import com.example.kbuddy_backend.qna.entity.QnaHeart;
import com.example.kbuddy_backend.user.constant.OAuthCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;
    private String email;
    private String password;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Authority> authorities = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Nullable
    private OAuthCategory oAuthCategory;

    @OneToMany(mappedBy = "user")
    private List<QnaHeart> qnaHeart = new ArrayList<>();

    public void addAuthority(Authority authority) {
        authorities.add(authority);
        authority.setUser(this);
    }
    @Builder
    public User(String username, String password, String email,
                OAuthCategory oAuthCategory) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.oAuthCategory = oAuthCategory;
    }
}
