package com.kos.CoCoCo.ja0.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.vo.ReplyVO;


public interface ReplyRepositoryH extends CrudRepository<ReplyVO, Long>{

	@Modifying
	@Query(value="delete from replies where board_board_id=?1", nativeQuery = true)
	public void deleteByBoardId(Long boardId);
}
