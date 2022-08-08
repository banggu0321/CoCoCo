package com.kos.CoCoCo.cansu.test;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.kos.CoCoCo.vo.BoardVO;
import com.kos.CoCoCo.vo.QBoardVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

//public interface BoardRepositoryTestSu extends CrudRepository<BoardVO, Long>, QuerydslPredicateExecutor<BoardVO> {

public interface BoardRepositoryTestSu extends PagingAndSortingRepository<BoardVO, Long>, QuerydslPredicateExecutor<BoardVO> {	
	
	//native query
	@Query(value="select*from boards where category_category_id=?1", nativeQuery = true)
	public BoardVO selectBoardByID(Long id);
	
	//findByIDIn
	@Query(value="select*from boards where category_category_id in (:category_category_id)", nativeQuery = true)
	public List<BoardVO> selectBoardByIDbeta(@Param("category_category_id") List<Long> id, Pageable pageable);
	
	//Querydsl
	public default Predicate makePredicate(String type, String keyword) {
		
		BooleanBuilder builder = new BooleanBuilder();		
		
		QBoardVO board = QBoardVO.boardVO;
		
		if(type ==null) {
			return builder;
		}
		
		switch(type) {
		case "t":
//			builder.and(board.isNotNull());
			break;
		}
		
		
		return builder;
	}

}
