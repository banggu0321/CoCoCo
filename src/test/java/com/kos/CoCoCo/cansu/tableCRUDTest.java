package com.kos.CoCoCo.cansu;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kos.CoCoCo.cansu.test.BoardCategoryRepository;
import com.kos.CoCoCo.cansu.test.BoardRepository;
import com.kos.CoCoCo.cansu.test.TeamRepository;
import com.kos.CoCoCo.cansu.test.TeamUserRepository;
import com.kos.CoCoCo.cansu.test.UserRepository;
import com.kos.CoCoCo.vo.BoardCategoryVO;
import com.kos.CoCoCo.vo.BoardVO;
import com.kos.CoCoCo.vo.TeamUserVO;
import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.UserVO;

@SpringBootTest
public class tableCRUDTest {

	@Autowired
	UserRepository userRP;
	
	@Autowired
	TeamRepository teamRP;
	
	@Autowired
	BoardCategoryRepository boardcateRP;
	
	@Autowired
	BoardRepository boardRP;

	
	@Test
	public void boardTest0718() {
		UserVO user = userRP.findById("0718").get();
		
		TeamVO team = teamRP.findById(1L).get();
		BoardCategoryVO bcTemp = boardcateRP.findByTeam(team);
		
		BoardVO boardTeamp = boardRP.findById(5L).get();
		
		System.out.println("user: "+user);
		System.out.println("team: "+team);
		System.out.println("baord-category: "+bcTemp);
		System.out.println("board: "+boardTeamp);
	}
	
//	@Test
//	public void boardInsert() {
//		TeamVO team = teamRP.findById(1L).get();
//		BoardCategoryVO bcTemp = boardcateRP.findByTeam(team);
//		System.out.println(bcTemp);
//		
//		UserVO user = userRP.findById("0718").get();
//		
//		BoardVO boardTemp = BoardVO.builder().category(bcTemp).user(user).boardTitle("20220718")
//				.build(); 
//		boardRP.save(boardTemp);
//	}
	
//	@Test
//	public void boardCategoryInsert() {
//		TeamVO teamvo = teamRP.findById(1L).get();
//		System.out.println(teamvo);
//		
////		BoardCategoryVO bcTemp = BoardCategoryVO.builder().teamId(teamvo.getTeamId()).team(teamvo).categoryName("sample").build();
////		System.out.println(bcTemp);
////		boardcateRP.save(bcTemp);
//	}
	
//	@Test
//	public void teamInsert() {
//		UserVO uservo = userRP.findById("0718").get();
//		TeamVO teamvo = TeamVO.builder().teamName("0718sample").user(uservo)
//				.build();
//		
//		//error (insert team_user table)
////		TeamUserVO temp = TeamUserVO.builder().team(teamvo).user(uservo).build(); 
//		
//		teamRP.save(teamvo);
////		teamUserRP.save(temp);
//	}
	
	
//	@Test
//	public void userSelect() {
//		List<UserVO> userList = (List<UserVO>) userRP.findAll();
//		userList.forEach(a->{
//			System.out.println(a);
//		});
//	}
	
//	@Test
//	public void userInsert() {
//		UserVO temp = UserVO.builder().userId("0718").pw("0718").name("kosta")
//				.build();
//		userRP.save(temp);
//	}
	
}
