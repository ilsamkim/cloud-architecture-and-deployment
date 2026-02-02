package com.cloudarchitectureanddeployment.controller;

import com.cloudarchitectureanddeployment.dto.MemberRequest;
import com.cloudarchitectureanddeployment.entity.Member;
import com.cloudarchitectureanddeployment.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    // 팀원 정보 저장
    @PostMapping("/api/members")
    public Member save(@RequestBody MemberRequest request) {
        return memberRepository.save(new Member(request.getName(), request.getAge(), request.getMbti()));
    }

    // 팀원 정보 조회
    @GetMapping("/api/members/{id}")
    public Member fintById(@PathVariable Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀원입니다. ID: " + id));
    }
}
