package hoxy.hLivv.service;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class S3Service {
    private final S3Template s3Template;

    private final String imageBucketName = "hlivv-image-bucket";

    @Value("${cloud.aws.region.static}")
    private String region;

    private final String[] allowedFileExtensions = new String[]{"jpg", "png", "gif"};

    /**
     * 이미지 파일을 S3에 업로드합니다.
     * jpg, png, gif 파일만 업로드 가능합니다.
     *
     * @param imageFile 업로드할 이미지 파일
     * @return 업로드된 이미지 파일의 URL
     * @throws S3FileUploadException  파일 업로드 실패
     * @throws MultipartFileException MultipartFile 예외
     */
    public String uploadImage(ImagePath imagePath, MultipartFile imageFile) throws IOException {
        final String fileName = imageFile.getOriginalFilename();
        try {
            Arrays.stream(allowedFileExtensions)
                  .filter(fileName::contains)
                  .findAny()
                  .orElseThrow(() -> new S3FileUploadException("File extension not allowed"));

            var uuid = UUID.randomUUID()
                           .toString();

            var result = s3Template.upload(imageBucketName, imagePath + "/" + uuid + fileName, imageFile.getInputStream(), ObjectMetadata.builder()
                                                                                                                                         .contentType(imageFile.getContentType())
                                                                                                                                         .build());

            if (!result.exists()) {
                throw new S3FileUploadException("File upload failed");
            }
            return result.getURL()
                         .toExternalForm();
        } catch (Exception e) {
            if (e instanceof S3FileUploadException) {
                throw e;
            } else {
                throw new MultipartFileException(e);
            }
        }
    }

    public void deleteImage(String imageUrl) {
        String[] splitted = new String[0];
        splitted = URLDecoder.decode(imageUrl, StandardCharsets.UTF_8)
                             .split("/");
        var key = new StringBuilder().append(splitted[splitted.length - 2])
                                     .append("/")
                                     .append(splitted[splitted.length - 1])
                                     .toString();
        s3Template.deleteObject(imageBucketName, key);

    }

    public enum ImagePath {
        REVIEW("review"),
        RESTORE("restore"),
        PROFILE("profile");

        private final String path;

        ImagePath(String path) {
            this.path = path;
        }

        @Override
        public String toString() {
            return path;
        }

    }

    /**
     * S3에 업로드 실패시 발생하는 Exception
     */
    public static class S3FileUploadException extends IOException {
        public S3FileUploadException(String message) {
            super(message);
        }
    }

    /**
     * MultipartFile 예외
     */
    public static class MultipartFileException extends IOException {
        private Exception e;

        public MultipartFileException(Throwable cause) {
            super(cause);
            this.e = e;
        }
    }

}
