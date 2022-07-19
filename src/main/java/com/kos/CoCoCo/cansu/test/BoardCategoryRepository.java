package com.kos.CoCoCo.cansu.test;

import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.vo.BoardCategoryMultikey;
import com.kos.CoCoCo.vo.BoardCategoryVO;
import com.kos.CoCoCo.vo.TeamVO;

public interface BoardCategoryRepository extends CrudRepository<BoardCategoryVO, BoardCategoryMultikey> {

	//query method
//	BoardCategoryVO findByTeam(TeamVO team);
}
