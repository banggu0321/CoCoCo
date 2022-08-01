package com.kos.CoCoCo.cansu.test;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.vo.BoardVO;

public interface BoardRepositoryTestSu extends CrudRepository<BoardVO, Long> {
	
	
	//native query
	@Query(value="select*from boards where category_category_id=?1", nativeQuery = true)
	public BoardVO selectBoardByID(Long id);

}
