package com.example.demo.bbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.bbs.dao.ReplyDAO;
import com.example.demo.bbs.vo.ReplyVO;
import com.example.demo.exceptions.PageNotFoundException;

@Service
public class ReplyServiceImpl implements ReplyService {
	@Autowired
	private ReplyDAO replyDAO;
	
	@Override
	public List<ReplyVO> getAllReplies(int boardId) {
		return replyDAO.getAllReplies(boardId);
	}

	@Override
	public boolean createNewReply(ReplyVO replyVO) {
		return replyDAO.createNewReply(replyVO) > 0;
	}

	@Override
	public boolean deleteOneReply(int replyId, String email) {
		ReplyVO replyVO = replyDAO.getOneReply(replyId);
		if (!email.equals(replyVO.getEmail())) {
			throw new PageNotFoundException("당신의 댓글이 아닙니다.");
		}
		
		return replyDAO.deleteOneReply(replyId) > 0;
	}

	@Override
	public boolean modifyOneReply(ReplyVO replyVO) {
		ReplyVO originalReplyVO = replyDAO.getOneReply(replyVO.getReplyId());
		if(!replyVO.getEmail().equals(originalReplyVO.getEmail())) {
			throw new PageNotFoundException("당신의 댓글이 아닙니다.");
		}
		
		return replyDAO.modifyOneReply(replyVO) > 0;
	}

	@Override
	public boolean recommendOneReply(int replyId, String email) {
		ReplyVO replyVO = replyDAO.getOneReply(replyId);
		if(!email.equals(replyVO.getEmail())) {
			throw new PageNotFoundException("당신의 댓글이 아닙니다.");
		}
		return replyDAO.recommendOneReply(replyId) > 0;
	}

}
