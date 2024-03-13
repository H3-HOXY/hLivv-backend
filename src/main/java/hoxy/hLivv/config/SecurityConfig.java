package hoxy.hLivv.config;


import hoxy.hLivv.jwt.JwtAccessDeniedHandler;
import hoxy.hLivv.jwt.JwtAuthenticationEntryPoint;
import hoxy.hLivv.jwt.JwtSecurityConfig;
import hoxy.hLivv.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * @author 이상원
 */
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(
            TokenProvider tokenProvider,
            CorsFilter corsFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//            .csrf(csrf -> csrf
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .ignoringRequestMatchers("/api/**") // API 경로에 대해서는 CSRF 보호 비활성화
//            )
// token을 사용하는 방식이기 때문에 csrf를 disable
.csrf(AbstractHttpConfigurer::disable)

.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
.exceptionHandling(exceptionHandling -> exceptionHandling
        .accessDeniedHandler(jwtAccessDeniedHandler)
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
)
.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
        .requestMatchers("/api/hello", "/api/login", "/api/signup", "/api/messageTransfer", "/api/product/**", "/api/category/**", "/api/collabo/**"
                , "/backoffice/login", "/backoffice/logout", "/backoffice/register", "/backoffice/signup", "/backoffice/css/**", "/backoffice/img/**", "/backoffice/js/**", "/backoffice/scss/**", "/backoffice/vendor/**"
                , "/swagger-ui/**", "/api-docs/**", "/swagger-ui.html")
        .permitAll()
        .anyRequest()
        .authenticated()
)
// 세션을 사용하지 않기 때문에 STATELESS로 설정
.sessionManagement(sessionManagement ->
        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
)
.headers(headers ->
        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
)
.with(new JwtSecurityConfig(tokenProvider), customizer -> {
});
        return http.build();
    }
}