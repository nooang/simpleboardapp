package com.example.demo.bbs.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.bbs.vo.BoardListVO;
import com.example.demo.bbs.vo.BoardVO;
import com.example.demo.bbs.vo.SearchBoardVO;

public interface BoardService {
	public BoardListVO getAllBoard(SearchBoardVO searchBoardVO);
	public boolean createNewBoard(BoardVO boardVO, MultipartFile file);
	public BoardVO getOneBoard(int id, boolean isIncrease);
	public boolean updateOneBoard(BoardVO boardVO, MultipartFile file);
	public boolean deleteOneBoard(int id);
}
