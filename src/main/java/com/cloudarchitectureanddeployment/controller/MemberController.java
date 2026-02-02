package com.cloudarchitectureanddeployment.controller;

import com.cloudarchitectureanddeployment.dto.MemberRequest;
import com.cloudarchitectureanddeployment.entity.Member;
import com.cloudarchitectureanddeployment.repository.MemberRepository;
import com.cloudarchitectureanddeployment.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    // 팀원 정보 저장
    @PostMapping
    public Member save(@RequestBody MemberRequest request) {
        return memberRepository.save(new Member(request.getName(), request.getAge(), request.getMbti()));
    }

    // 팀원 정보 조회
    @GetMapping("/{id}")
    public Member findById(@PathVariable Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀원입니다. ID: " + id));
    }

    // 이미지 업로드
    @PostMapping("/{id}/profile-image")
    public ResponseEntity<Void> uploadProfileImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        memberService.uploadProfileImage(id, file);
        return ResponseEntity.ok().build();
    }

    // Presigned URL 발급
    @GetMapping("/{id}/profile-image")
    public ResponseEntity<String> getProfileImage(@PathVariable Long id) {
        String url = memberService.generatePresignedUrl(id);
        return ResponseEntity.ok(url);
    }
}
