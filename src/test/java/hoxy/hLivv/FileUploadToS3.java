package hoxy.hLivv;


import hoxy.hLivv.service.S3Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
@SpringBootTest
public class FileUploadToS3 {

    @Autowired
    S3Service s3Service;


    @Test
    void fileUpload() throws IOException {

        MultipartFile multipartFile =  LocalFileToMultipartFileConverter.convert(
                "C:\\Users\\sangwon\\Desktop\\최종플젝\\현대lnc 이미지\\칸스톤 주방상판 리모델링 엔지니어드 스톤 싱크대 상판@50000@상판\\167962858275319687.avif");
        System.out.println("MultipartFile: " + multipartFile.getName());
//        s3Service.uploadImage(S3Service.ImagePath.PRODUCT, );
    }

    public class LocalFileToMultipartFileConverter {

        public static MultipartFile convert(String filePath) throws IOException {
            File file = new File(filePath);

            // 파일을 BufferedImage로 읽어들임
            BufferedImage bufferedImage = readImage(file);

            // BufferedImage를 MultipartFile로 변환
            MultipartFile multipartFile = bufferedImageToMultipartFile(bufferedImage);

            return multipartFile;
        }

        private static BufferedImage readImage(File file) throws IOException {
            // 파일 확장자를 확인하여 적절한 ImageIO 리더를 선택하여 이미지를 읽어들임
            String formatName = getFileExtension(file.getName());
            ImageInputStream iis = ImageIO.createImageInputStream(file);
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(formatName);
            ImageReader reader = readers.next();
            reader.setInput(iis);
            return reader.read(0);
        }

        private static String getFileExtension(String fileName) {
            return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        }

        private static MultipartFile bufferedImageToMultipartFile(BufferedImage image) throws IOException {
            // BufferedImage를 File로 변환
            File tempFile = File.createTempFile("temp", null);
            ImageIO.write(image, "png", tempFile);

            // File을 MultipartFile로 변환
            FileInputStream input = new FileInputStream(tempFile);
            MultipartFile multipartFile = new MockMultipartFile(tempFile.getName(), input);

            // 임시 파일 삭제
            tempFile.delete();

            return multipartFile;
        }
    }

}
