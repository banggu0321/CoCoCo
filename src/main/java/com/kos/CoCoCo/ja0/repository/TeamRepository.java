package com.kos.CoCoCo.ja0.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.vo.TeamVO;


public interface TeamRepository extends CrudRepository<TeamVO, Long>{
	
	List<TeamVO> findByInviteCode(String inviteCode);
}
