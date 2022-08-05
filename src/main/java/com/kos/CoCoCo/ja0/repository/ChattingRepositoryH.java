package com.kos.CoCoCo.ja0.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.vo.ChattingVO;


public interface ChattingRepositoryH extends CrudRepository<ChattingVO, Long>{

	@Query(value = "select * from chatting where user_user_id = ?1", nativeQuery = true)
	List<ChattingVO> findByUserId(String userId);
	
	@Modifying
	@Query(value = "delete from chatting where team_team_id = ?1", nativeQuery = true)
	void deleteByTeamId(Long teamId);
}
