package com.kos.CoCoCo.cansu.test;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.vo.ReplyVO;
import com.kos.CoCoCo.vo.UserVO;

public interface ReplyRepositoryTestSu extends CrudRepository<ReplyVO, Long> {

	Long findByUser(UserVO user);
	
	//@query
	@Query(value="select*from replies where board_board_id=?1 order by reply_reg_date desc", nativeQuery = true)
	List<ReplyVO> selectByboardID(int id);
}
