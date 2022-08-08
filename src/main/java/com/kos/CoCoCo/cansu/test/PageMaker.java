package com.kos.CoCoCo.cansu.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.java.Log;

@Getter
@ToString(exclude = "pageList")
@Log
public class PageMaker {
	
	private Page result;
	private List<Pageable> pageList;
	
	private Pageable prevPage;  //이전 pageable 정보
	private Pageable nextPage;  //다음 pageable 정보
	private Pageable currentPage;
	
	private int currentPageNum;
	private int totalPageNum;
	
	public PageMaker(Page result) {
		this.result = result;
		this.currentPage = result.getPageable();
		this.currentPageNum = currentPage.getPageNumber()+1;
		this.totalPageNum = result.getTotalPages();
		this.pageList = new ArrayList<>();
		calcPages();
//		calcPagesBeta();
	}
	
	private void calcPagesBeta() {
		//보여줄 페이지 개수 설정
	}

	private void calcPages() {
		int tempEndNum = (int)(Math.ceil(this.currentPageNum/10.0)*10);
		int startNum = tempEndNum - 9;
		Pageable startPage = this.currentPage;
				
		//move to start Pageble
		for(int i=startNum; i<this.currentPageNum; i++) {
			startPage = startPage.previousOrFirst();
			System.out.println("calcPage.startPage: "+startPage);
		}
		this.prevPage = startPage.getPageNumber() <= 0 ? null : startPage.previousOrFirst();
		
		if(this.totalPageNum < tempEndNum) {
			tempEndNum = this.totalPageNum;
			this.nextPage = null;
		}
		
		for(int i=startNum; i<=tempEndNum; i++) {
			pageList.add(startPage);
			startPage = startPage.next();
		}
		this.nextPage = startPage.getPageNumber()+1 <= totalPageNum? startPage: null;
		
		
//		System.out.println("tempEndNum: "+tempEndNum);
//		System.out.println("startNum: "+startNum);
//		
//		System.out.println("startPage.getPageNumber: "+startPage.getPageNumber());
//		System.out.println("totalPageNum: "+totalPageNum);
//		
//		System.out.println("nextPage: "+nextPage);
		//System.out.println("nextPage.getPageNumber(): "+nextPage.getPageNumber());
	}
}
