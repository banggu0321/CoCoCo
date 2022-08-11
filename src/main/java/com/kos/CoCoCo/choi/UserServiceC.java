package com.kos.CoCoCo.choi;

import com.kos.CoCoCo.vo.UserVO;

public interface UserServiceC {
	// 아이디 체크
	public UserVO idCheck(String id);
	
	// 비밀번호 체크
	public int pwCheck(String pw, String userId);
	
}