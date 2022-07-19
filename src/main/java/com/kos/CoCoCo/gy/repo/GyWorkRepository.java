package com.kos.CoCoCo.gy.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.WorkVO;

public interface GyWorkRepository extends CrudRepository<WorkVO, Long>{
	
	public List<WorkVO> findByTeam(TeamVO team);
	
	public List<WorkVO> findByWorkId(Long workId);
	//@Query(value = "SELECT WORK_ID , WORK_TITLE , WORK_START , WORK_END , WORK_STATUS FROM WORKS w WHERE TEAM_TEAM_ID =?1" , nativeQuery = true)

}
