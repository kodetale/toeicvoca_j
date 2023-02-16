package com.voca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voca.entity.Member;
import com.voca.repository.MemberRepository;
import com.voca.repository.WrongRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

	@Autowired
	private final MemberRepository memberRepository;
	private final WrongRepository wrongRepository;

	public Member saveMember(Member member) {
		validateDuplicateMember(member);
		return memberRepository.save(member);
	}

	private void validateDuplicateMember(Member member) {
		Member findMember = memberRepository.findByMemberId(member.getMemberId());

		if (findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}

	}

	@Override
	public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
		Member member = memberRepository.findByMemberId(memberId);

		if (member == null) {
			throw new UsernameNotFoundException(memberId);
		}

		return User.builder().username(member.getMemberId()).password(member.getPassword())
				.roles(member.getRole().toString()).build();
	}

	public Member findbyMemberId(String memberId) {
		return memberRepository.findByMemberId(memberId);
	}

	@Transactional
	public void editMember(Member member, PasswordEncoder passwordEncoder) {

		Member editMember = memberRepository.findById(member.getId()).orElseThrow(() -> {
			return new IllegalArgumentException("가입되지 않은 회원입니다.");
		});

		if (!member.getPassword().isEmpty()) {
			String rawPassword = member.getPassword();
			String enPassword = passwordEncoder.encode(rawPassword);
			editMember.setPassword(enPassword);
		}

		editMember.setName(member.getName());
	}

	@Transactional
	public void delMember(Member member) {

		wrongRepository.deleteByMemberId(member.getMemberId());
		memberRepository.deleteById(member.getId());
	}

}