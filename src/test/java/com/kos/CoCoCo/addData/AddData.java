package com.kos.CoCoCo.addData;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kos.CoCoCo.gy.GyUserRepository;
import com.kos.CoCoCo.vo.UserVO;

@SpringBootTest
public class AddData {
	@Autowired
	GyUserRepository userRepo;

	//@Test
	public void test1() {
		UserVO user = UserVO.builder()
				.userId("bang@naver.com")
				.pw("1234")
				.name("방근영")
				.company("회사")
				.image("guitar-playing.png")
				.status("status")
				.build();
		userRepo.save(user);
	}
}
