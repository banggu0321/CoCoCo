package com.kos.CoCoCo.choi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kos.CoCoCo.vo.UserVO;

@Controller
@RequestMapping("/user")
public class UserControllerC {

    @Autowired
    @Qualifier("userService")
    private UserServiceC userService;

    // 아이디 체크
    @PostMapping("/idCheck")
    @ResponseBody
    public int idCheck(@RequestParam("userId") String userId){
    	UserVO user = userService.idCheck(userId);
    	System.out.println(user);
    	return user==null?0:1;

    }
}

//1.Controller ->2.Service ->3.Repository ->4.Mapper
//5.Mapper ->6.Repository ->7.Service  ->8.Controller