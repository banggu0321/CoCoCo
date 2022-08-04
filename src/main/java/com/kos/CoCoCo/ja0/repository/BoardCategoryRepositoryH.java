package com.kos.CoCoCo.ja0.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.vo.BoardCategoryMultikey;
import com.kos.CoCoCo.vo.BoardCategoryVO;


public interface BoardCategoryRepositoryH extends CrudRepository<BoardCategoryVO, BoardCategoryMultikey>{

	@Modifying
	@Query(value = "delete from board_category where team_team_id = ?1", nativeQuery = true)
	void deleteByTeamId(Long teamId);
}
