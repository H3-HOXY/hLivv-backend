package hoxy.hLivv.backoffice.controller;

import hoxy.hLivv.dto.member.MemberDto;
import hoxy.hLivv.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author 이상원
 */
@ControllerAdvice(assignableTypes = {BackofficeController.class})
@RequiredArgsConstructor
public class BackofficeControllerAdvice {

    private final MemberService memberService;


    @ModelAttribute
    public void addAttributes(HttpServletRequest request, Model model) {
        // 현재 요청의 URL을 가져옵니다.
        String requestURI = request.getRequestURI();

        if (!requestURI.startsWith("/backoffice/login") && !requestURI.startsWith("/backoffice/register")) {
            MemberDto memberDto = memberService.getMyMemberWithAuthorities();
            model.addAttribute("adminMemberDto", memberDto);
        }
    }
}