package com.cloudarchitectureanddeployment.repository;

import com.cloudarchitectureanddeployment.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
