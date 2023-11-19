package com.example.demo.bbs.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.bbs.service.BoardService;
import com.example.demo.bbs.vo.BoardListVO;
import com.example.demo.bbs.vo.BoardVO;
import com.example.demo.bbs.vo.SearchBoardVO;
import com.example.demo.beans.FileHandler;
import com.example.demo.exceptions.FileNotExistsException;
import com.example.demo.exceptions.MakXlsxFileException;
import com.example.demo.exceptions.PageNotFoundException;
import com.example.demo.exceptions.UserIdentifyNotMatchException;
import com.example.demo.member.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class BoardController {
	private Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired
	private BoardService boardService;
	@Autowired
	private FileHandler fileHandler;
	
	@GetMapping("/board/list")
	public ModelAndView viewBoardList(@ModelAttribute SearchBoardVO searchBoardVO) {
		ModelAndView mav = new ModelAndView("board/boardlist");
		BoardListVO boardList = boardService.getAllBoard(searchBoardVO);
		searchBoardVO.setPageCount(boardList.getBoardCnt());
		
		mav.addObject("boardList", boardList);
		mav.addObject("searchBoardVO", searchBoardVO);
		return mav;
	}
	
	@GetMapping("/board/write")
	public String viewBoardWritePage() {
		return "board/boardwrite";
	}
	
	@PostMapping("/board/write")
	public ModelAndView doBoardWrite(@Valid @ModelAttribute BoardVO boardVO,
									 BindingResult bindingResult,
									 HttpServletRequest request,
									 @RequestParam MultipartFile file,
									 @SessionAttribute("_LOGIN_USER_") MemberVO memberVO) {
		logger.debug("이메일: " + boardVO.getEmail());
		
		ModelAndView mav = new ModelAndView("redirect:/board/list");
		boardVO.setIpAddr(request.getRemoteAddr());
		boardVO.setEmail(memberVO.getEmail());
		
		if (bindingResult.hasErrors()) {
			mav.setViewName("board/boardwrite");
			mav.addObject("boardVO", boardVO);
			return mav;
		}
		
		boolean isSuccess = boardService.createNewBoard(boardVO, file);
		
		if (isSuccess) {
			return mav;
		}
		else {
			mav.setViewName("board/boardwrite");
			mav.addObject("boardVO", boardVO);
			return mav;
		}
	}
	
	@GetMapping("/board/view")
	public ModelAndView viewOneBoard(@RequestParam int id) {
		BoardVO boardVO = boardService.getOneBoard(id, true);
		ModelAndView mav = new ModelAndView("board/boardview");
		
		mav.addObject("boardVO", boardVO);
		return mav;
	}
	
	@GetMapping("/board/file/download/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable int id) {
		BoardVO boardVO = boardService.getOneBoard(id, false);
		if (boardVO == null) {
			throw new PageNotFoundException("없는 게시글 입니다.");
		}
		
		File storedFile = fileHandler.getStoredFile(boardVO.getFileName());
		
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION,
				   "attachment; filename=" + boardVO.getOriginFileName());
		
		InputStreamResource resource;
		try {
			resource = new InputStreamResource(new FileInputStream(storedFile));
		} catch (FileNotFoundException e) {
			throw new FileNotExistsException("파일이 존재하지 않습니다.");
		}
		
		return ResponseEntity.ok()
				.headers(header)
				.contentLength(storedFile.length())
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(resource);
	}
	
	@GetMapping("/board/modify/{id}")
	public ModelAndView viewBoardModifyPage(@PathVariable int id,
											@SessionAttribute("_LOGIN_USER_") MemberVO memberVO) {
		BoardVO boardVO = boardService.getOneBoard(id, false);
		ModelAndView mav = new ModelAndView("board/boardmodify");
		
		if (!boardVO.getEmail().equals(memberVO.getEmail())) {
			throw new UserIdentifyNotMatchException(memberVO, "당신의 게시글이 아닙니다.");
		}
		
		mav.addObject("boardVO", boardVO);
		return mav;
	}
	
	@PostMapping("/board/modify")
	public ModelAndView doBoardUpdate(@Valid @ModelAttribute BoardVO boardVO,
									  BindingResult bindingResult,
									  @RequestParam MultipartFile file,
									  @SessionAttribute("_LOGIN_USER_") MemberVO memberVO) {
		ModelAndView mav = new ModelAndView("redirect:/board/view?id=" + boardVO.getId());
		boardVO.setEmail(memberVO.getEmail());
		
		if(bindingResult.hasErrors()) {
			mav.setViewName("board/boardmodify");
			mav.addObject("boardVO", boardVO);
			return mav;
		}
		
		BoardVO originBoardVO = boardService.getOneBoard(boardVO.getId(), false);
		if (!originBoardVO.getEmail().equals(memberVO.getEmail())) {
			throw new UserIdentifyNotMatchException(memberVO, "당신의 게시글이 아닙니다.");
		}
		
		boolean isSuccess = boardService.updateOneBoard(boardVO, file);
		if(isSuccess) {
			return mav;
		}
		else {
			mav.setViewName("board/boardmodify");
			mav.addObject("boardVO", boardVO);
			return mav;
		}
	}
	
	@GetMapping("/board/delete/{id}")
	public String doDeleteBoard(@PathVariable int id,
								@SessionAttribute("_LOGIN_USER_") MemberVO memberVO) {
		BoardVO originBoardVO = boardService.getOneBoard(id, false);
		if (!originBoardVO.getEmail().equals(memberVO.getEmail())) {
			throw new UserIdentifyNotMatchException(memberVO, "당신의 게시글이 아닙니다.");
		}
		
		boolean isSuccess = boardService.deleteOneBoard(id);
		
		if (isSuccess) {
			return "redirect:/board/list";
		}
		else {
			return "redirect:/board/view?id=" + id;
		}
	}
	
	@GetMapping("/board/excel/download")
	public ResponseEntity<Resource> donwloadExcelFile() {
		BoardListVO boardListVO = boardService.getAllBoard(null);
		
		Workbook workbook = new SXSSFWorkbook(-1);
		Sheet sheet = workbook.createSheet("게시글 목록");
		Row row = sheet.createRow(0);
		
		Cell cell = row.createCell(0);
		cell.setCellValue("번호");
		cell = row.createCell(1);
		cell.setCellValue("제목");
		cell = row.createCell(2);
		cell.setCellValue("첨부파일명");
		cell = row.createCell(3);
		cell.setCellValue("작성자이메일");
		cell = row.createCell(4);
		cell.setCellValue("조회수");
		cell = row.createCell(5);
		cell.setCellValue("등록일");
		cell = row.createCell(6);
		cell.setCellValue("수정일");
		
		List<BoardVO> boardList = boardListVO.getBoardList();
		int rowIndex = 1;
		
		for (BoardVO boardVO : boardList) {
			row = sheet.createRow(rowIndex);
			cell = row.createCell(0);
			cell.setCellValue("" + boardVO.getId());
			cell = row.createCell(1);
			cell.setCellValue(boardVO.getSubject());
			cell = row.createCell(2);
			cell.setCellValue(boardVO.getOriginFileName());
			cell = row.createCell(3);
			cell.setCellValue(boardVO.getEmail());
			cell = row.createCell(4);
			cell.setCellValue(boardVO.getViewCnt());
			cell = row.createCell(5);
			cell.setCellValue(boardVO.getCrtDt());
			cell = row.createCell(6);
			cell.setCellValue(boardVO.getMdfyDt());
			
			rowIndex += 1;
		}
		
		File storedFile = fileHandler.getStoredFile("게시글_목록.xlsx");
		OutputStream os = null;
		
		try {
			os = new FileOutputStream(storedFile);
			workbook.write(os);
		} catch (IOException e) {
			throw new MakXlsxFileException("엑셀 파일을 만들 수 없습니다.");
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {}
			if (os != null) {
				try {
					os.flush();					
				} catch (IOException e) {}
				try {
					os.close();
				} catch (IOException e) {}
			}
		}
		
		String downloadFileName = URLEncoder.encode("게시글목록.xlsx", Charset.defaultCharset());
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "force-download"));
		header.setContentLength(storedFile.length());
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + downloadFileName);
		
		InputStreamResource resource;
		try {
			resource = new InputStreamResource(new FileInputStream(storedFile));
		} catch (FileNotFoundException e) {
			throw new FileNotExistsException("파일이 존재하지 않습니다.");
		}
		
		return ResponseEntity.ok()
				.headers(header)
				.body(resource);
	}
}
