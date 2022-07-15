package com.kos.CoCoCo.cansu;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
public class testDBconnet {
	
	@Autowired
	MemberRepository member;
	
	
//	@Test
//	public void f1() {
//		IntStream.rangeClosed(1, 10).forEach(a->{
//			MemberVO temp = MemberVO.builder().mid(String.valueOf(a)).mpassword("0714").mname("2022")
//					.build();
//			member.save(temp);
//		});
//	}
	
//	@Test
//	public void f1Beta() {
//		//select all
//		List<MemberVO> mlist = (List<MemberVO>)member.findAll();
//		mlist.forEach(a->{
//			System.out.println(a);
//		});
//	}
}
