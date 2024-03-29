package com.kos.CoCoCo.cansu.test;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageVO {
	
	private static final int DEFAULT_SIZE=5;
	private static final int DEFAULT_MAX_SIZE=50;
	
	private int page;
	private int size;
	String type;
	String keyword;
	
	public PageVO() {
		this.page = 1;
		this.size = DEFAULT_SIZE;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page<0 ? 1 : page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size < DEFAULT_SIZE || size > DEFAULT_MAX_SIZE ? DEFAULT_SIZE:size;
	}
	
	public Pageable makePageable(int direction, String... props) {
		Sort.Direction dir = direction == 0 ? Sort.Direction.DESC : Sort.Direction.ASC;
		return PageRequest.of(this.page-1, this.size, dir, props);
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
