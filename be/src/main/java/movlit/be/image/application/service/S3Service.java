package movlit.be.image.application.service;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.exception.ImageUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public String uploadImage(MultipartFile file, String folderName) {
        String fileName = generateFileName(file.getOriginalFilename(), folderName);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        try {
            log.info("Uploading file to S3 with key: {}", fileName);
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            log.error("Error uploading file to S3", e);
            throw new ImageUploadException();
        }

        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toExternalForm();
    }

    public String generateFileName(String originalFilename, String folderName) {
        String sanitizedFilename = sanitizeFileName(originalFilename);
        return folderName + "/" + UUID.randomUUID() + "-" + sanitizedFilename;
    }

    private String sanitizeFileName(String originalFilename) {
        if (originalFilename == null) {
            return "unknown";
        }
        // 알파벳, 숫자, 점(.), 대시(-), 언더스코어(_)만 허용
        return originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-_]", "");
    }

}
