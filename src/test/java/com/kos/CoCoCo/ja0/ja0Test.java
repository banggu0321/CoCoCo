package com.kos.CoCoCo.ja0;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kos.CoCoCo.vo.UserVO;

@SpringBootTest
public class ja0Test {

	@Autowired
	UserRepository uRepo;
	
	@Test
	public void test() {
		UserVO user = UserVO.builder()
							.company("Kosta")
							.name("홍자영")
							.pw("1234")
							.userId("ja0@naver.com")
							.build();
		uRepo.save(user);
	}
}
