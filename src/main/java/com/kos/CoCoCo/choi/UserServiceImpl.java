package com.kos.CoCoCo.choi;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kos.CoCoCo.vo.UserVO;

@Service("userService")
public class UserServiceImpl implements UserServiceC {

	@Autowired
	private UserRepositoryC repo;

	@Autowired
	PasswordEncoder passwordEncoder;


	// 아이디 중복 체크
	@Override
    public UserVO idCheck(String id) {
       UserVO user = repo.findById(id).orElse(null);
        return user;
	}
        
    // 비밀번호 체크
	@Override
	public int pwCheck(String pw, String userId) {
		String pwd = passwordEncoder.encode(pw);
		int result = 0;
		Optional<UserVO> user = repo.findById(userId);
		System.out.println(user);
		 if(user.isPresent()) {
			 UserVO u = user.get();
			 System.out.println(u);
			 if(u.getPw().equals(pwd)) result = 1;
		 }
		return result;
	}

}
