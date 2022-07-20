package com.kos.CoCoCo.cansu.test;

import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.vo.TeamUserMultikey;
import com.kos.CoCoCo.vo.TeamUserVO;

public interface TeamUserRepositoryTestSu extends CrudRepository<TeamUserVO, TeamUserMultikey> {

}
