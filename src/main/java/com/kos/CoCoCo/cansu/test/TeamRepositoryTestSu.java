package com.kos.CoCoCo.cansu.test;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.vo.TeamVO;

public interface TeamRepositoryTestSu extends CrudRepository<TeamVO, Long> {
	
	
	//query
	@Query(value="select*from teams where user_user_id=?1", nativeQuery = true)
	TeamVO selectByUserID(String userid);

}
