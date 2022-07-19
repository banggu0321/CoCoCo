package com.kos.CoCoCo.ja0;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kos.CoCoCo.ja0.repository.UserRepository;
import com.kos.CoCoCo.vo.UserVO;

@SpringBootTest
public class ja0Test {

	@Autowired
	UserRepository uRepo;
	
	//@Test
	public void test2() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 20;
		Random random = new Random();
		String generatedString = random.ints(leftLimit, rightLimit + 1)
		                                   .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
		                                   .limit(targetStringLength)
		                                   .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
		                                   .toString();
		System.out.println(generatedString);
	}
	//@Test
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
