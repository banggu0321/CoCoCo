package com.kos.CoCoCo.ja0.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.kos.CoCoCo.vo.QTeamUserVO;
import com.kos.CoCoCo.vo.TeamUserMultikey;
import com.kos.CoCoCo.vo.TeamUserVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;


public interface TeamUserRepository 
		extends PagingAndSortingRepository<TeamUserVO, TeamUserMultikey>, 
				QuerydslPredicateExecutor<TeamUserVO> {

	@Query(value = "select * from team_user where team_team_id = ?1 order by user_role, join_date",
		nativeQuery = true)
	List<TeamUserVO> findByTeamId(Long teamId);
	
	@Query(value = "select * from team_user where user_user_id = ?1 order by 1",
			nativeQuery = true)
	List<TeamUserVO> findByUserId(String userId);
	
	@Modifying
	@Query(value = "delete from team_user where team_team_id = ?1", nativeQuery = true)
	void deleteByTeamId(Long teamId);
	
	public default Predicate makePredicate(String UserId) {
		BooleanBuilder builder = new BooleanBuilder();
		QTeamUserVO teamUser = QTeamUserVO.teamUserVO;
		// 검색조건처리
		return builder.and(teamUser.teamUserId.user.userId.eq(UserId));
	}
}
