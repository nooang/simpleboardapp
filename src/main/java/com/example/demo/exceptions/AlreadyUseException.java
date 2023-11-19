package com.example.demo.exceptions;

import com.example.demo.member.vo.MemberVO;

public class AlreadyUseException extends RuntimeException {

	private static final long serialVersionUID = -119187109323309202L;
	
	private MemberVO memberVO;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public AlreadyUseException(MemberVO memberVO, String message) {
		super();
		this.memberVO = memberVO;
	}
	
	public MemberVO getMemberVO() {
		return memberVO;
	}	
}
