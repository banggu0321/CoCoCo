package com.kos.CoCoCo.sol.repository;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.kos.CoCoCo.vo.NoticeVO;
import com.kos.CoCoCo.vo.QNoticeVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;


public interface NoticeRepository extends PagingAndSortingRepository<NoticeVO, Long>,
 QuerydslPredicateExecutor<NoticeVO> {
	
	List<NoticeVO> findByFixedYN(String fixedYN);
	
   	public default Predicate makePredicate(String type, String keyword, String keyword2) {
		BooleanBuilder builder = new BooleanBuilder();
		QNoticeVO notice = QNoticeVO.noticeVO;
		
		builder.and(notice.noticeId.gt(0));
		System.out.println(type + keyword);
		//검색조건처리
		if(type==null) return builder;
		switch (type) {
		case "noticeTitleAndText" : 
			builder.and(notice.noticeText.like("%" + keyword + "%"));
			builder.or(notice.noticeTitle.like("%" + keyword + "%"));
			break;
		case "noticeTitle": builder.and(notice.noticeTitle.like("%" + keyword + "%")); break;
		case "noticeText": builder.and(notice.noticeText.like("%" + keyword + "%")); break;
		case "noticeRegDate": 
			Timestamp sdate, edate;
			sdate = convertDate(keyword);
			edate = convertDate(keyword2);
			builder.and(notice.noticeRegDate.between(sdate, edate)); break;
			
		default: break;
		}
		System.out.println(builder);
		return builder;
		}
	
	public default Timestamp convertDate(String strDate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date d;
		Timestamp ts = null;
		try {
			d = dateFormat.parse(strDate);
			ts = new Timestamp(d.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ts;
		
	}
	
}