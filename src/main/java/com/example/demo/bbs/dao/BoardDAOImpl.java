package com.example.demo.bbs.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.bbs.vo.BoardVO;
import com.example.demo.bbs.vo.SearchBoardVO;

@Repository
public class BoardDAOImpl extends SqlSessionDaoSupport implements BoardDAO {
	@Autowired
	@Override
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	
	@Override
	public int getBoardAllCount() {
		return getSqlSession().selectOne("getBoardAllCount");
	}

	@Override
	public List<BoardVO> getAllBoard() {
		return getSqlSession().selectList("getAllBoard");
	}
	
	@Override
	public List<BoardVO> searchAllBoard(SearchBoardVO searchBoardVO) {
		return getSqlSession().selectList("searchAllBoard", searchBoardVO);
	}

	@Override
	public int createNewBoard(BoardVO boardVO) {
		return getSqlSession().insert("createNewBoard", boardVO);
	}


	@Override
	public int increaseViewCount(int id) {
		return getSqlSession().update("increaseViewCount", id);
	}


	@Override
	public BoardVO getOneBoard(int id) {
		return getSqlSession().selectOne("getOneBoard", id);
	}


	@Override
	public int updateOneBoard(BoardVO boardVO) {
		return getSqlSession().update("updateOneBoard", boardVO);
	}


	@Override
	public int deleteOneBoard(int id) {
		return getSqlSession().delete("deleteOneBoard", id);
	}
	
}
