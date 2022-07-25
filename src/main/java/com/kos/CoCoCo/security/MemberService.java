package com.kos.CoCoCo.security;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kos.CoCoCo.choi.UserRepositoryC;
import com.kos.CoCoCo.vo.UserVO;

@Service
public class MemberService implements UserDetailsService{
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private UserRepositoryC mrepo;
	
	@Autowired
	PasswordEncoder passwordEncoder; // security config에서 Bean생성
	// 회원가입
	@Transactional
	public UserVO joinUser(UserVO member) {
		// 비밀번호 암호화...암호화되지않으면 로그인되지않는다.
		String pwd = passwordEncoder.encode(member.getPw());
		member.setPw(pwd);
		System.out.println("암호화된 pass:" + pwd);
		return mrepo.save(member);
	}

	//!!!!반드시 구현해야한다. 
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException {
		System.out.println("loadUserByUsername mid:" + mid);
		//filter는 조건에 맞는것만 선택
		//map: 변형한다. 
	 
		UserDetails member = mrepo.findById(mid)
				.filter(m -> m != null).map(m -> new SecurityUser(m)).get();
		System.out.println("UserDetails member:" + member);
		httpSession.setAttribute("user", member);
		return member;
	}

	 
	
}
 