package com.example.demo.bbs.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.bbs.dao.BoardDAO;
import com.example.demo.bbs.vo.BoardListVO;
import com.example.demo.bbs.vo.BoardVO;
import com.example.demo.bbs.vo.SearchBoardVO;
import com.example.demo.beans.FileHandler;
import com.example.demo.beans.FileHandler.StoredFile;
import com.example.demo.exceptions.PageNotFoundException;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardDAO boardDAO;
	@Autowired
	private FileHandler fileHandler;
	
	
	@Override
	public BoardListVO getAllBoard(SearchBoardVO searchBoardVO) {
		BoardListVO boardList = new BoardListVO();
		boardList.setBoardCnt(boardDAO.getBoardAllCount());
		
		if (searchBoardVO == null) {
			boardList.setBoardList(boardDAO.getAllBoard());			
		}
		else {
			boardList.setBoardList(boardDAO.searchAllBoard(searchBoardVO));						
		}
		
		return boardList;
	}
	
	@Transactional
	@Override
	public boolean createNewBoard(BoardVO boardVO, MultipartFile file) {
		StoredFile storedFile = fileHandler.storeFile(file);
		boardVO.setFileName(storedFile.getRealFileName());
		boardVO.setOriginFileName(storedFile.getFileName());
		return boardDAO.createNewBoard(boardVO) > 0;
	}
	
	@Transactional
	@Override
	public BoardVO getOneBoard(int id, boolean isIncrease) {
		if (isIncrease) {
			int updateCount = boardDAO.increaseViewCount(id);
			
			if (updateCount == 0) {
				throw new PageNotFoundException("조회수 증가에 실패했습니다. 없는 게시글 번호일지도?");
			}			
		}
		
		BoardVO boardVO = boardDAO.getOneBoard(id);
		
		if (boardVO == null) {
			throw new PageNotFoundException("게시글 정보가 비어있습니다.");
		}
		
		return boardVO;
	}

	@Transactional
	@Override
	public boolean updateOneBoard(BoardVO boardVO, MultipartFile file) {
		if (file != null && !file.isEmpty()) {
			BoardVO originBoardVO = boardDAO.getOneBoard(boardVO.getId());
			if (originBoardVO != null && originBoardVO.getFileName() != null) {
				File originFile = fileHandler.getStoredFile(originBoardVO.getFileName());
				if (originFile.exists() && originFile.isFile()) {
					originFile.delete();
				}
			}
			
			StoredFile storedFile = fileHandler.storeFile(file);
			boardVO.setFileName(storedFile.getRealFileName());
			boardVO.setOriginFileName(storedFile.getFileName());
		}
		
		return boardDAO.updateOneBoard(boardVO) > 0;
	}

	@Transactional
	@Override
	public boolean deleteOneBoard(int id) {
		BoardVO originBoardVO = boardDAO.getOneBoard(id);
		if (originBoardVO != null && originBoardVO.getFileName() != null) {
			File originFile = fileHandler.getStoredFile(originBoardVO.getFileName());
			if (originFile.exists() && originFile.isFile()) {
				originFile.delete();
			}
		}
		return boardDAO.deleteOneBoard(id) > 0;
	}

}
