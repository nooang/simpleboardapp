package com.example.demo.member.service;

import com.example.demo.member.vo.MemberVO;

public interface MemberService {
	public boolean createNewMember(MemberVO memberVO);
	public boolean checkAvailableEmail(String email);
	public MemberVO getMember(MemberVO memberVO);
	public boolean deleteMe(String email);
}
