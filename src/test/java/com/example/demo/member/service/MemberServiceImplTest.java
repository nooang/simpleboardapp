package com.example.demo.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.exceptions.AlreadyUseException;
import com.example.demo.member.dao.MemberDAO;
import com.example.demo.member.dao.MemberDAOImpl;
import com.example.demo.member.vo.MemberVO;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import({MemberServiceImpl.class, MemberDAOImpl.class})
public class MemberServiceImplTest {
	@Autowired
	private MemberService memberService;
	
	@MockBean
	private MemberDAO memberDAO;
	
	@Test
	@DisplayName("회원 ID 중복체크 테스트")
	public void checkAvailableEmailTest() {
		
		given(memberDAO.getEmailCount("user01@gmail.com"))
				.willReturn(0);
		
		given(memberDAO.getEmailCount("user02@gmail.com"))
				.willReturn(1);
		
		boolean isAvailableEmail = memberService.checkAvailableEmail("user01@gmail.com");
		
		assertTrue(isAvailableEmail);
		
		boolean isNotAvailableEmail = memberService.checkAvailableEmail("user02@gmail.com");
		assertFalse(isNotAvailableEmail);
		
		verify(memberDAO).getEmailCount("user01@gmail.com");
		verify(memberDAO).getEmailCount("user02@gmail.com");
	}
	
	@Test
	@DisplayName("회원가입 실패 테스트")
	public void memberRegistFailTest() {
		given(memberDAO.getEmailCount("user02@gmail.com"))
				.willReturn(1);
		
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("user02@gmail.com");
		
		AlreadyUseException exception = assertThrows(AlreadyUseException.class, () -> memberService.createNewMember(memberVO));
		
		String expectMessage = "Email이 이미 사용중입니다.";
		assertEquals(expectMessage, exception.getMessage());
		
		verify(memberDAO).getEmailCount("user02@gmail.com");
	}
	
	@Test
	@DisplayName("회원가입 성공 테스트")
	public void memberRegistSuccessTest() {
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("user02@gmail.com");
		memberVO.setName("테스트사용자");
		memberVO.setPassword("testpassword");
		
		given(memberDAO.getEmailCount(memberVO.getEmail()))
				.willReturn(0);
		
		given(memberDAO.createNewMember(memberVO))
				.willReturn(1);
		
		boolean isSuccess = memberService.createNewMember(memberVO);
		
		assertTrue(isSuccess);
		assertNotNull(memberVO.getSalt());
		assertNotNull(memberVO.getPassword());
		
		verify(memberDAO).getEmailCount(memberVO.getEmail());
		verify(memberDAO).createNewMember(memberVO);
	}
}
