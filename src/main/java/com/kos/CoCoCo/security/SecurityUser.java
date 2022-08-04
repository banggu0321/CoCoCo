package com.kos.CoCoCo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.kos.CoCoCo.vo.UserVO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class SecurityUser extends User{
	private static final long serialVersionUID = 1L;
	private static final String ROLE_PREFIX="ROLE_USER";
    private UserVO member;   
	public SecurityUser(String name, String password, Collection<? extends GrantedAuthority> authorities) {
		super(name, password, authorities);
	}
	public SecurityUser(UserVO member) {	
		super(member.getUserId(), member.getPw(), makeRole(member)  );
		this.member = member;
		System.out.println("SecurityUser member:" + member);
	}
	//Role을 여러개 가질수 있도록 되어있음 
	private static List<GrantedAuthority> makeRole(UserVO member) {
		List<GrantedAuthority> roleList = new ArrayList<>();
		roleList.add(new SimpleGrantedAuthority(ROLE_PREFIX  ));
		return roleList;
	}
//	 
	//User class에서 username필드가 있지만 google 인증시 사용되는 필드는 name
	//이를 맞추기위해 함수추가함 
	public String getName() {
		// TODO Auto-generated method stub
		return super.getUsername();
	}
	
	
}
