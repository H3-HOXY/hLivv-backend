package hoxy.hLivv.backoffice.controller;


import hoxy.hLivv.dto.LoginDto;
import hoxy.hLivv.dto.TokenDto;
import hoxy.hLivv.jwt.JwtFilter;
import hoxy.hLivv.jwt.TokenProvider;
import hoxy.hLivv.service.MemberService;
import hoxy.hLivv.util.SecurityUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/backoffice")
@Slf4j
@RequiredArgsConstructor
public class BackofficeController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberService memberService;


    @GetMapping("/register")
    public String signupPage() {
        return "backoffice/register";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute LoginDto loginDto) {
        return "backoffice/login";
    }

    @PostMapping("/login")
    public void authorize(@Valid @RequestBody LoginDto loginDto,
                          HttpServletResponse response) {

        log.info(loginDto.toString());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getLoginPw());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        // 쿠키에 JWT 추가
        Cookie cookie = new Cookie(JwtFilter.AUTHORIZATION_HEADER, jwt);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(24 * 60 * 60); // 쿠키 유효 시간 설정 (24시간)
        cookie.setPath("/");
        response.addCookie(cookie);


        // 리다이렉트 또는 다른 방법으로 클라이언트에 응답
//        return ResponseEntity.ok().headers(httpHeaders).body("backoffice/home");
    }

    @GetMapping("/home")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String home(Model model) {
        Optional<String> username = SecurityUtil.getCurrentUsername();
        model.addAttribute("memberDto", memberService.getMyMemberWithAuthorities());


        return "backoffice/home";
    }

    @GetMapping("/members")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String members() {
        return "backoffice/members";
    }

    @GetMapping("/auth")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String auth() {
        return "backoffice/request_auth";
    }
}