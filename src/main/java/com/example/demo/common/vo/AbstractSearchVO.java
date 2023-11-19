package com.example.demo.common.vo;

public class AbstractSearchVO {
	private int pageNo;
	private int listSize;
	private int pageCount;
	private int pageCountInGroup;
	private int groupCount;
	private int groupNo;
	private int groupStartPageNo;
	private int groupEndPageNo;
	private boolean hasNextGroup;
	private boolean hasPrevGroup;
	private int nextGroupStartPageNo;
	private int prevGroupStartPageNo;
	
	
	public AbstractSearchVO() {
		this.listSize = 10;
		this.pageCountInGroup = 10;
	}


	public int getPageNo() {
		return pageNo;
	}


	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}


	public int getListSize() {
		return listSize;
	}


	public void setListSize(int listSize) {
		this.listSize = listSize;
	}


	public int getPageCount() {
		return pageCount;
	}


	public void setPageCount(int listCount) {
		this.pageCount = (int) Math.ceil((double) listCount / this.listSize);
		this.groupCount = (int) Math.ceil((double) this.pageCount / this.pageCountInGroup);
		this.groupNo = this.pageNo / this.pageCountInGroup;
		this.groupStartPageNo = this.groupNo * this.pageCountInGroup;
		this.groupEndPageNo = (this.groupNo + 1) * this.pageCountInGroup - 1;
		if (groupEndPageNo > this.pageCount - 1) {
			this.groupEndPageNo = this.pageCount - 1;
		}
		this.hasNextGroup = this.groupNo + 1 < this.groupCount;
		this.hasPrevGroup = this.groupNo > 0;
		this.nextGroupStartPageNo = this.groupEndPageNo + 1;
		this.prevGroupStartPageNo = this.groupStartPageNo - this.pageCountInGroup;
	}


	public int getPageCountInGroup() {
		return pageCountInGroup;
	}


	public void setPageCountInGroup(int pageCountInGroup) {
		this.pageCountInGroup = pageCountInGroup;
	}


	public int getGroupCount() {
		return groupCount;
	}


	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}


	public int getGroupNo() {
		return groupNo;
	}


	public void setGroupNo(int groupNo) {
		this.groupNo = groupNo;
	}


	public int getGroupStartPageNo() {
		return groupStartPageNo;
	}


	public void setGroupStartPageNo(int groupStartPageNo) {
		this.groupStartPageNo = groupStartPageNo;
	}


	public int getGroupEndPageNo() {
		return groupEndPageNo;
	}


	public void setGroupEndPageNo(int groupEndPageNo) {
		this.groupEndPageNo = groupEndPageNo;
	}


	public boolean isHasNextGroup() {
		return hasNextGroup;
	}


	public void setHasNextGroup(boolean hasNextGroup) {
		this.hasNextGroup = hasNextGroup;
	}


	public boolean isHasPrevGroup() {
		return hasPrevGroup;
	}


	public void setHasPrevGroup(boolean hasPrevGroup) {
		this.hasPrevGroup = hasPrevGroup;
	}


	public int getNextGroupStartPageNo() {
		return nextGroupStartPageNo;
	}


	public void setNextGroupStartPageNo(int nextGroupStartPageNo) {
		this.nextGroupStartPageNo = nextGroupStartPageNo;
	}


	public int getPrevGroupStartPageNo() {
		return prevGroupStartPageNo;
	}


	public void setPrevGroupStartPageNo(int prevGroupStartPageNo) {
		this.prevGroupStartPageNo = prevGroupStartPageNo;
	}
}
