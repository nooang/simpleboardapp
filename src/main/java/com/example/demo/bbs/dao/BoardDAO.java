package com.example.demo.bbs.dao;

import java.util.List;

import com.example.demo.bbs.vo.BoardVO;
import com.example.demo.bbs.vo.SearchBoardVO;

public interface BoardDAO {
	public int getBoardAllCount();
	public List<BoardVO> getAllBoard();
	public List<BoardVO> searchAllBoard(SearchBoardVO searchBoardVO);
	public int createNewBoard(BoardVO boardVO);
	public int increaseViewCount(int id);
	public BoardVO getOneBoard(int id);
	public int updateOneBoard(BoardVO boardVO);
	public int deleteOneBoard(int id);
}
