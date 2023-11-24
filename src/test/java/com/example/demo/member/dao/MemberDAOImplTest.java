package com.example.demo.member.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import com.example.demo.member.vo.MemberVO;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MemberDAOImpl.class)
public class MemberDAOImplTest {
	@Autowired
	private MemberDAO memberDAO;
	
	@Test
	public void getEmailCountTest() {
		int count = memberDAO.getEmailCount("user01@gmail.com");
		assertEquals(count, 0);
	}
	
	@Test
	public void createNewMemberTest() {
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail("testuser01@gmail.com");
		memberVO.setName("테스트사용자");
		memberVO.setPassword("testpassword");
		memberVO.setSalt("testsalt");
		
		int count = memberDAO.createNewMember(memberVO);
		assertEquals(count, 1);
	}
	
	@Test
	public void deleteMeTest() {
		int count = memberDAO.deleteMe("user01@gmail.com");
		assertEquals(count, 0);
	}
}
