package com.kos.CoCoCo.sol.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.WorkVO;

public interface WorkRepository extends CrudRepository<WorkVO, Long> {
	
	public List<WorkVO> findByTeam(TeamVO team);

}
