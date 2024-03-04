package hoxy.hLivv.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class SMTPServiceTests {

    @Autowired
    AmazonSMTPService amazonSMTPService;

    @Test
    @DisplayName("Amazon SES로 이메일 전송하기")
    void send() {
        String subject = "제목입니다.";
        String to = "kumct12@naver.com";
        Map<String, Object> variables = new HashMap<>();
        variables.put("productName", "상품 이름");
        variables.put("requestGrade", "A");
        variables.put("inspectedGrade", "B");
        variables.put("payback", "10000");
        variables.put("rejectMsg", "B등급으로 판명되었음.");
        amazonSMTPService.sendRestoreCompleteEmail(subject, variables, to);
    }
}
