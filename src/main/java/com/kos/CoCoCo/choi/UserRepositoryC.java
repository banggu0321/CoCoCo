package com.kos.CoCoCo.choi;

import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.vo.UserVO;

public interface UserRepositoryC extends CrudRepository<UserVO, String>{
		//1.가지 ....findAll(), findById()
	    //2.규칙에 맞는 함수정의 findByName()
	    //3.@Query
}