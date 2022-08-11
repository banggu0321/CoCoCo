package com.kos.CoCoCo.choi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.kos.CoCoCo.vo.UserVO;

@Service("userService")
public class UserServiceImpl implements UserServiceC {

	@Autowired
	private UserRepositoryC repo;

	// 아이디 중복 체크
	@Override
    public UserVO idCheck(String id) {
       UserVO user = repo.findById(id).orElse(null);
        return user;
	}
        
    // 비밀번호 중복 체크
	@Override
	public UserVO pwCheck(String pw) {
		UserVO user = repo.findById(pw).orElse(null);
		return user;
	}

}
