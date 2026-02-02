package com.cloudarchitectureanddeployment.service;

import com.cloudarchitectureanddeployment.entity.Member;
import com.cloudarchitectureanddeployment.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final MemberRepository memberRepository;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    // 이미지 업로드
    @Transactional
    public void uploadProfileImage(Long memberId, MultipartFile file) throws IOException {
        String fileKey = "profile/" + memberId + "_" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        member.updateProfileImageKey(fileKey);
    }

    // Presigned URL 발급
    public String generatePresignedUrl(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        String fileKey = member.getProfileImageKey();
        if (fileKey == null) throw new IllegalStateException("등록된 프로필 이미지가 없습니다.");

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofDays(7)) // 유효기간 7일 확인
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}
