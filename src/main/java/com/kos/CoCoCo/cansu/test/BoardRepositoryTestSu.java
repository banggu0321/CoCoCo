package com.kos.CoCoCo.cansu.test;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.vo.BoardVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public interface BoardRepositoryTestSu extends CrudRepository<BoardVO, Long>, QuerydslPredicateExecutor<BoardVO> {
	
	
	//native query
	@Query(value="select*from boards where category_category_id=?1", nativeQuery = true)
	public BoardVO selectBoardByID(Long id);
	
	//Querydsl
	public default Predicate makePredicate(String type, String keyword) {
		
		BooleanBuilder builder = new BooleanBuilder();		
		return null;
	}

}
