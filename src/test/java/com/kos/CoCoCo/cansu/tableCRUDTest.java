package com.kos.CoCoCo.cansu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kos.CoCoCo.cansu.test.BoardCategoryRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.BoardRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.ReplyRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.TeamRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.TeamUserRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.UserRepositoryTestSu;
import com.kos.CoCoCo.vo.BoardCategoryMultikey;
import com.kos.CoCoCo.vo.BoardCategoryVO;
import com.kos.CoCoCo.vo.BoardVO;
import com.kos.CoCoCo.vo.ReplyVO;
import com.kos.CoCoCo.vo.TeamUserMultikey;
import com.kos.CoCoCo.vo.TeamUserVO;
import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.UserVO;

@SpringBootTest
public class tableCRUDTest {

	@Autowired
	UserRepositoryTestSu userRP;
	
	@Autowired
	TeamRepositoryTestSu teamRP;
	
	@Autowired
	TeamUserRepositoryTestSu teamUserRP;
	
	@Autowired
	BoardCategoryRepositoryTestSu boardcateRP;
	
	@Autowired
	BoardRepositoryTestSu boardRP;
	
	@Autowired
	ReplyRepositoryTestSu replyRP;

	
	@Test
	public void boardSelectByCategoryID() {
		List<Long>categoryID = boardcateRP.selectIDByname("sample2022");
		
		List<BoardVO> boardList = new ArrayList<>();
		categoryID.forEach(a->{
			BoardVO temp = boardRP.selectBoardByID(a);
			if(temp != null) {
				boardList.add(temp);
			}
		});
		
		boardList.forEach(a->{
			System.out.println(a);
		});
	}
	
//	@Test 
//	public void categorySelectName() {
//		List<String> list = boardcateRP.selectAllCategoryName();
////		list.forEach(a->{
////			System.out.println(a);
////		});
////		
////		for(String temp: list) {
////			System.out.println(temp);
////		}
//		
//		for(int i=0; i<list.size(); i++) {
//			System.out.println(list.get(i));
//		}
//	}

//	@Test
//	public void categorySelectAll() {
//		List<BoardCategoryVO> list = (List<BoardCategoryVO>)boardcateRP.findAll();
//		
//		for(BoardCategoryVO temp: list) {
//			System.out.println("categoryID: "+temp.getBoardCategoryId().getCategoryId());
//			System.out.println("teamID: "+temp.getBoardCategoryId().getTeam().getTeamId());
//			System.out.println("category name: "+temp.getCategoryName());
//		}
//	}
	
//	@Test
//	public void replySelectByboardID() {
//		int boardID = 61;
//		
//		List<ReplyVO> rlist = replyRP.selectByboardID(boardID);
//		rlist.forEach(a->{
//			System.out.println(a);
//		});
//	}

//	@Test
//	public void replyDelete() {
////		UserVO uservo = userRP.findById("0720").get();  //log in info check delete able
//		
//		ReplyVO reply = replyRP.findById(47L).get();  //selected reply
//		replyRP.delete(reply);		
//	}
	
//	@Test
//	public void boardUpdate() {
//		
////		UserVO uservo = userRP.findById("0720").get();  //log in info check board update able
//		BoardVO boardTemp = boardRP.findById(46L).get();  //selected board
//
//		boardTemp.setBoardTitle("updateTitle");
//		boardTemp.setBoardText("updateText");
//		boardRP.save(boardTemp);
//	}
	
//	@Test
//	public void repliesInsert() {
//		
//		UserVO uservo = userRP.findById("0720").get();  //log in
//		BoardVO boardTemp = boardRP.findById(61L).get();  //selected board
//		System.out.println(uservo);
//		System.out.println(boardTemp);
//		
//		ReplyVO rvo = ReplyVO.builder().board(boardTemp).user(uservo).replyText("0721").build();
//		replyRP.save(rvo);
//	}
	
	
//	@Test
//	public void boardInsert() {	
//		// board category insert + board insert
//		long gnrTemp =  new Random().nextLong();
//		if(gnrTemp <0) {
//			gnrTemp = -1*gnrTemp;
//		}
//		long gnrValue = Long.valueOf(String.valueOf(gnrTemp).substring(0, 6));
////		System.out.println(gnrValue);
//		
//		UserVO uservo = userRP.findById("0720").get();
//		TeamVO teamvo =  teamRP.selectByUserID(uservo.getUserId());
//		System.out.println(teamvo);
//		
//		//error(not one row)
////		BoardCategoryMultikey bcMultikey = BoardCategoryMultikey.builder().categoryId(244050L).team(teamvo).build();
////		BoardCategoryVO bcvo = boardcateRP.findById(bcMultikey).get();
////		System.out.println(bcvo);
//		
//		BoardCategoryMultikey bctemp = BoardCategoryMultikey.builder().categoryId(gnrValue).team(teamvo).build();
//		BoardCategoryVO bcvotemp = BoardCategoryVO.builder().boardCategoryId(bctemp).categoryName("sample3").build();
////		System.out.println(bcvotemp);
//		boardcateRP.save(bcvotemp);
//
//		
//		BoardVO boardTemp = BoardVO.builder().category(bcvotemp).user(uservo).boardTitle("sample0720").build(); 
//		boardRP.save(boardTemp);
//	}
	
//	@Test
//	public void boardCategoryInsert() {
//		//BoardCategoryMultikey -> generateValue not work
//		long gnrTemp =  new Random().nextLong();
//		if(gnrTemp <0) {
//			gnrTemp = -1*gnrTemp;
//		}
//		long gnrValue = Long.valueOf(String.valueOf(gnrTemp).substring(0, 6));
////		System.out.println(gnrValue);
//		
//		UserVO uservo = userRP.findById("0720").get();
//		TeamVO teamvo =  teamRP.selectByUserID(uservo.getUserId());
//		System.out.println(teamvo);
//		
//		BoardCategoryMultikey bcMultikey = BoardCategoryMultikey.builder().categoryId(gnrValue).team(teamvo).build();
//		BoardCategoryVO bcTemp = BoardCategoryVO.builder().boardCategoryId(bcMultikey).categoryName("sample0720").build();
//		boardcateRP.save(bcTemp);
//		
////		System.out.println(bcMultikey);
//	}
	
//	@Test
//	public void teamInsert() {
////		UserVO uservo = userRP.findById("0720").get();
////		TeamVO teamvo = TeamVO.builder().teamName("0720sample").user(uservo)
////				.build();
////		teamRP.save(teamvo);
//		
//		// (insert team_user table)
////		UserVO uservo = userRP.findById("0720").get();
////		TeamVO teamteamp = teamRP.selectByUserID(uservo.getUserId());
////		
////		TeamUserMultikey tuMultikey = TeamUserMultikey.builder().team(teamteamp).user(uservo).build();
////		TeamUserVO temp = TeamUserVO.builder().teamUserId(tuMultikey).userRole("sample").build(); 
////		
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
//		UserVO temp = UserVO.builder().userId("0720").pw("0720").name("kosta")
//				.build();
//		userRP.save(temp);
//	}
	
}
