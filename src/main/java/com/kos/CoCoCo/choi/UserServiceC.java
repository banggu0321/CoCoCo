package com.kos.CoCoCo.choi;

import com.kos.CoCoCo.vo.UserVO;

public interface UserServiceC {
    // 아이디 중복확인
    public UserVO idCheck(String id);
}