package com.example.kbuddy_backend.common.config;

import static com.example.kbuddy_backend.user.constant.UserRole.NORMAL_USER;

import com.example.kbuddy_backend.auth.dto.response.AccessTokenAndRefreshTokenResponse;
import com.example.kbuddy_backend.auth.service.AuthService;
import com.example.kbuddy_backend.qna.entity.Qna;
import com.example.kbuddy_backend.qna.entity.QnaCategory;
import com.example.kbuddy_backend.qna.repository.QnaCategoryRepository;
import com.example.kbuddy_backend.qna.repository.QnaRepository;
import com.example.kbuddy_backend.user.constant.Country;
import com.example.kbuddy_backend.user.constant.Gender;
import com.example.kbuddy_backend.user.entity.Authority;
import com.example.kbuddy_backend.user.entity.User;
import com.example.kbuddy_backend.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 서비스 운영 시 해당 클레스 제외
 * */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final QnaRepository qnaRepository;
    private final QnaCategoryRepository qnaCategoryRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initData() {
        // 더미 사용자 생성
        final String password = passwordEncoder.encode("kbuddy");
        User user = User.builder()
                .email("kbuddy@gmail.com")
                .username("johnhuh")
                .password(password)
                .firstName("tony")
                .lastName("stark")
                .gender(Gender.M)
                .bio("hello, i'm tony stark.")
                .country(Country.KOREA)
                .build();
        user.addAuthority(new Authority(NORMAL_USER));
        User saveUser = userRepository.save(user);

        List<GrantedAuthority> grantedAuthorities = saveUser.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName().name()))
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(saveUser.getEmail(), saveUser.getPassword(), grantedAuthorities);
        AccessTokenAndRefreshTokenResponse token = authService.createToken(authenticationToken);
        log.info("테스트용 토큰 입니다.: {}", token.accessToken());


        // QnA 카테고리 생성
        QnaCategory qnaCategory1 = new QnaCategory("VISA");
        QnaCategory qnaCategory2 = new QnaCategory("FOOD");
        QnaCategory qnaCategory3 = new QnaCategory("HOSPITAL");
        qnaCategoryRepository.save(qnaCategory1);
        qnaCategoryRepository.save(qnaCategory2);
        qnaCategoryRepository.save(qnaCategory3);

        // 더미 Qna 생성
        Qna qna = Qna.builder()
                .title("Dummy Title")
                .description("Dummy Description")
                .category(qnaCategory1)
                .writer(user)
                .build();
        qnaRepository.save(qna);



    }
}
