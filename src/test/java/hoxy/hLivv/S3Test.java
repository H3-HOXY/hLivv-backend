package hoxy.hLivv;

import hoxy.hLivv.service.S3Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StreamUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class S3Test {

    @Autowired
    private S3Client s3Client;

    @Test
    public void testReadFileFromS3() throws IOException {
        String keyName = "/description/a.jpg";

        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(
                request -> request.bucket("hlivv-s3-bucket")
                                  //Root '/'는 표기하지 않습니다.
                                  .key("description/Hello World!.txt"));

        String fileContent = StreamUtils.copyToString(response, StandardCharsets.UTF_8);

        System.out.println(fileContent);
        assertNotNull(fileContent);
    }
}