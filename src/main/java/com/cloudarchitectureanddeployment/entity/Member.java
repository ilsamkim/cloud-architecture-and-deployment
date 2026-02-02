package com.cloudarchitectureanddeployment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String mbti;

    private String profileImageKey;

    public Member(String name, int age, String mbti) {
        this.name = name;
        this.age = age;
        this.mbti = mbti;
    }

    public void updateProfileImageKey(String profileImageKey){
        this.profileImageKey = profileImageKey;
    }
}
