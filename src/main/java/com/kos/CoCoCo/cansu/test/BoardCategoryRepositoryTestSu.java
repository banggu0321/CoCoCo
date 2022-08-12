package com.kos.CoCoCo.cansu.test;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.vo.BoardCategoryMultikey;
import com.kos.CoCoCo.vo.BoardCategoryVO;
import com.kos.CoCoCo.vo.TeamVO;

public interface BoardCategoryRepositoryTestSu extends CrudRepository<BoardCategoryVO, BoardCategoryMultikey> {

	@Query(value="select * from board_category where category_id =?1", nativeQuery = true)
	public BoardCategoryVO selectByCategoryId(Long categoryId);
	
	@Query(value="select * from board_category where team_team_id=?1", nativeQuery = true)
	public List<BoardCategoryVO> selectByTeam(Long team_id);
	
	//native query
	@Query(value="select distinct category_name from board_category", nativeQuery = true)
	public List<String> selectAllCategoryName();
	
	@Query(value="select*from board_category where category_name=?1", nativeQuery = true)
	public List<Long> selectIDByname(String name);

}
