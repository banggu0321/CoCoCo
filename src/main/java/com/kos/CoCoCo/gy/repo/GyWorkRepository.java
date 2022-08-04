package com.kos.CoCoCo.gy.repo;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.WorkVO;

public interface GyWorkRepository extends CrudRepository<WorkVO, Long>{
	
	public List<WorkVO> findByTeam(TeamVO team);

	@Query(value="SELECT * FROM WORKS w JOIN WORK_MANAGER wm ON (wm.work_work_id = w.work_id) WHERE wm.USER_USER_ID=?1", nativeQuery = true)
	public List<WorkVO> findByUser(String user_id);
	
	//public WorkVO findByWorkId(Long workId);
	//@Query(value = "SELECT WORK_ID , WORK_TITLE , WORK_START , WORK_END , WORK_STATUS FROM WORKS w WHERE TEAM_TEAM_ID =?1" , nativeQuery = true)

}
