package com.example.demo.exceptions;

import com.example.demo.member.vo.MemberVO;

public class UserIdentifyNotMatchException extends RuntimeException {

	private static final long serialVersionUID = 8455110692549489572L;
	
	private MemberVO memberVO;

	public UserIdentifyNotMatchException(MemberVO memberVO, String message) {
		super();
		this.memberVO = memberVO;
	}
	
	public MemberVO getMemberVO() {
		return memberVO;
	}
}
