package com.example.demo.bbs.vo;

import com.example.demo.common.vo.AbstractSearchVO;

public class SearchBoardVO extends AbstractSearchVO {
	private String searchType;
	private String searchKeyword;
	
	
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
}
