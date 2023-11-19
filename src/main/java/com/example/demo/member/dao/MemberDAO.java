package com.example.demo.member.dao;

import com.example.demo.member.vo.MemberVO;

public interface MemberDAO {
	public int getEmailCount(String email);
	public int createNewMember(MemberVO memberVO);
	public String getSalt(String email);
	public MemberVO getMember(MemberVO memberVO);
	public int successLogin(MemberVO memberVO);
	public int failLogin(MemberVO memberVO);
	public int blockMember(String email);
	public int deleteMe(String email);
}
