package com.voca.entity;

import com.voca.constant.Role;
import com.voca.dto.MemberDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.persistence.*;

@Entity
@Table(name="member")
@Getter @Setter
@ToString
public class Member{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "memberid", unique = true)
    private String memberId;
    
    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
    
    public static Member createMember(MemberDto memberDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setMemberId(memberDto.getMemberId());
        String password = passwordEncoder.encode(memberDto.getPassword());
        member.setPassword(password);
        member.setName(memberDto.getName());
        member.setRole(Role.USER);
        return member;
    }

}
