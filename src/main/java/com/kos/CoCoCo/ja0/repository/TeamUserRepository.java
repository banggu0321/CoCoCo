package com.kos.CoCoCo.ja0.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.vo.TeamUserMultikey;
import com.kos.CoCoCo.vo.TeamUserVO;


public interface TeamUserRepository extends CrudRepository<TeamUserVO, TeamUserMultikey>{

	@Query(value = "select * from team_user where team_team_id = ?1 order by user_role",
		nativeQuery = true)
	List<TeamUserVO> findByTeamId(Long teamId);
	
	@Query(value = "select * from team_user where user_user_id = ?1 order by 1",
			nativeQuery = true)
	List<TeamUserVO> findByUserId(String userId);
	
	@Modifying
	@Query(value = "delete from team_user where team_team_id = ?1", nativeQuery = true)
	void deleteByTeamId(Long teamId);
	
}
