package hoxy.hLivv.service;


import hoxy.hLivv.util.MessageByteCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Service
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:message-transfer.properties")
public class MessageTransferService {
    @Value("${message.url}")
    private String url;
    @Value("${message.smsSendType}")
    private int sms;
    @Value("${message.mmsSendType}")
    private int mms;

    public void messageTransfer(String toNumber, String contents) {

        int sendType;


        if (MessageByteCalculator.byteCalc(contents) > 90) {
            sendType = mms;
        } else {
            sendType = sms;
        }
        log.info("contents byte = {}", MessageByteCalculator.byteCalc(contents));
        String encodedContents = URLEncoder.encode(contents, StandardCharsets.UTF_8);

        String messageTransferUrl = String.format(
                url,
                sendType, toNumber, encodedContents);

        try {
            URL url = new URL(messageTransferUrl); // 여기에 요청할 URL을 입력하세요.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET"); // 요청 방식 선택 (GET, POST, 등)
//            conn.setRequestProperty("Accept", "application/json"); // 헤더 설정 (필요한 경우)

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
