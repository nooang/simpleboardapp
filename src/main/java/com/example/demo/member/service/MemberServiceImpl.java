package com.example.demo.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.beans.SHA;
import com.example.demo.exceptions.AlreadyUseException;
import com.example.demo.exceptions.UserIdentifyNotMatchException;
import com.example.demo.member.dao.MemberDAO;
import com.example.demo.member.vo.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	private MemberDAO memberDAO;
	@Autowired
	private SHA sha;
	
	@Transactional
	@Override
	public boolean createNewMember(MemberVO memberVO) {
		int emailCount = memberDAO.getEmailCount(memberVO.getEmail());
		if(emailCount > 0) {
			throw new AlreadyUseException(memberVO, "Email이 이미 사용중입니다.");
		}
		
		String salt = sha.generateSalt();
		String password = memberVO.getPassword();
		String encryptedPassword = sha.getEncrypt(password, salt);
		memberVO.setPassword(encryptedPassword);
		memberVO.setSalt(salt);
		
		return memberDAO.createNewMember(memberVO) > 0;
	}

	@Override
	public boolean checkAvailableEmail(String email) {
		return memberDAO.getEmailCount(email) == 0;
	}

	@Override
	public MemberVO getMember(MemberVO memberVO) {
		String salt = memberDAO.getSalt(memberVO.getEmail());
		if(salt == null) {
			throw new UserIdentifyNotMatchException(memberVO, "사용자 정보가 일치하지 않습니다.");
		}
		
		String password = memberVO.getPassword();
		String encryptedPassword = sha.getEncrypt(password, salt);
		memberVO.setPassword(encryptedPassword);
		
		MemberVO member = memberDAO.getMember(memberVO);
		if (member == null) {
			memberDAO.failLogin(memberVO);
			memberDAO.blockMember(memberVO.getEmail());
			throw new UserIdentifyNotMatchException(memberVO, "아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		
		if (member.getBlockYn().equalsIgnoreCase("Y")) {
			throw new UserIdentifyNotMatchException(memberVO, "차단된 아이디입니다.");
		}
		
		
		memberDAO.successLogin(memberVO);
		return member;
	}

	@Transactional
	@Override
	public boolean deleteMe(String email) {
		return memberDAO.deleteMe(email) > 0;
	}
}
