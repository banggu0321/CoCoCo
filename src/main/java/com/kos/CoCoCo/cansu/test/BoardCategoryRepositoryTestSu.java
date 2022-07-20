package com.kos.CoCoCo.cansu.test;

import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.vo.BoardCategoryMultikey;
import com.kos.CoCoCo.vo.BoardCategoryVO;
import com.kos.CoCoCo.vo.TeamVO;

public interface BoardCategoryRepositoryTestSu extends CrudRepository<BoardCategoryVO, BoardCategoryMultikey> {

}
