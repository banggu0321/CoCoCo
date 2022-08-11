package com.kos.CoCoCo.cansu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.kos.CoCoCo.cansu.test.BoardCategoryRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.BoardRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.PageMaker;
import com.kos.CoCoCo.cansu.test.PageVO;
import com.kos.CoCoCo.cansu.test.ReplyRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.TeamRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.TeamUserRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.UserRepositoryTestSu;
import com.kos.CoCoCo.vo.BoardCategoryMultikey;
import com.kos.CoCoCo.vo.BoardCategoryVO;
import com.kos.CoCoCo.vo.BoardVO;
import com.kos.CoCoCo.vo.QBoardVO;
import com.kos.CoCoCo.vo.ReplyVO;
import com.kos.CoCoCo.vo.TeamUserMultikey;
import com.kos.CoCoCo.vo.TeamUserVO;
import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.UserVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

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
	public void selectBoardBybuildBeta() {
		Long teamId = 1761L;
		
		List<BoardVO> boards = boardRP.selectBoardByteam(teamId);	
		System.out.println("boards size: "+boards.size());
		
		List<BoardVO> result = new ArrayList<>();
		for(BoardVO temp:boards) {
			if(temp.getBoardTitle().contains("hello")) {
				result.add(temp);
			}
		}
		System.out.println("result size: "+result.size());
	}
	
//	@Test
//	public void selectBoardBybuild() {
		
//		String title = "hello";
//		Long id=1761L;
//		
//		List<BoardVO> board = boardRP.selectBoardByTitle(title);
//		System.out.println("board.size: "+board.size());
//		
//		BooleanBuilder builder = new BooleanBuilder();		
//		QBoardVO qboard = QBoardVO.boardVO;	
//		builder.and(qboard.boardTitle.contains(title));
//		builder.and(qboard.category.boardCategoryId.categoryId(id)); // error
//		Predicate predicate = builder;
//		
//		List<BoardVO> boards = (List<BoardVO>) boardRP.findAll(predicate);
//		System.out.println("size: "+boards.size());
		
		
//		System.out.println("test start");
//		List<BoardVO> boardList = boardRP.findByBoardTitleContainingAndBoardCategoryVOEquals(title, bcVO);
//		System.out.println("boardList: "+boardList.size());
//		boardList.forEach(a->{
//			System.out.println(a);
//		});
		
//		Pageable page = PageRequest.of(0, 5);
//		Page<BoardVO> boards = boardRP.findAll(predicate, page);
//		List<BoardVO> boardList = boards.getContent();
		
//		Predicate predicate = boardRP.makePredicate("t", "hello");		
//		List<BoardVO> boardList = (List<BoardVO>) boardRP.findAll(predicate);
//		
//		System.out.println("test start");
//		System.out.println("board list length: "+boardList.size());
//		boardList.forEach(a->{
//			System.out.println(a);
//		});
//	}

	
//	@Test
//	public void selectBoardByteamid() {
//		
//		
//		List<BoardVO> boards = boardRP.selectBoardByteam(1761L);
//		boards.forEach(a->{
//			System.out.println(a);
//		});
//		
//		Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "board_id");
//		Page<BoardVO> result2 = new PageImpl<BoardVO>(boards, pageable, boards.size());
//		List<BoardVO> temp = result2.getContent();
//		temp.forEach(a->{
//			System.out.println(a);
//		});
//		new PageMaker(result2);
//		
////		PageVO vo = new PageVO();
////		Pageable page = vo.makePageable(0, "boardId");
////		Page<BoardVO>  result = boardRP.findAll(boardRP.makePredicate("a", "1761"), page);
////		
////		result.forEach(a->{
////			System.out.println(a);
////		});
//	}
	
//	@Test
//	public void deletBoardWithReply() {
//		List<ReplyVO> reply = replyRP.findByboardBoardId(1881L);
//		reply.forEach(a->{
//			System.out.println(a);
//		});
//	}
	
//	@Test
//	public void delteReply() {
//	UserVO uservo = userRP.findById("su0809").get();  //log in info check delete able
//	System.out.println(uservo);
//	
////	ReplyVO reply = replyRP.findById(47L).get();  //selected reply
////	replyRP.delete(reply);	
//	}

//	@Test
//	public void selectBoardBycategoryID() {
//		List<Long> categoryID = new ArrayList<>();
//		categoryID.add(459222L);
//		categoryID.add(997132L);
//		categoryID.add(351303L);
//		categoryID.add(412403L);
//		
//		System.out.println(categoryID);
//		
////		List<BoardVO> boardList = boardRP.selectBoardByIDbeta(categoryID,null);
////		System.out.println(boardList);
//	}
	
//	@Test
//	public void insertBoard() {
//		
//		IntStream.rangeClosed(1, 100).forEach(a->{
//			
//		// board category insert + board insert
//		long gnrTemp =  new Random().nextLong();
//		if(gnrTemp <0) {
//			gnrTemp = -1*gnrTemp;
//		}
//		long gnrValue = Long.valueOf(String.valueOf(gnrTemp).substring(0, 6));
////		System.out.println(gnrValue);
//		
//		UserVO uservo = userRP.findById("su0804").get();
//		TeamVO teamvo =  teamRP.selectByUserID(uservo.getUserId());
//		System.out.println(teamvo);
//		
//		BoardCategoryMultikey bctemp = BoardCategoryMultikey.builder().categoryId(gnrValue).team(teamvo).build();
//		BoardCategoryVO bcvotemp = BoardCategoryVO.builder().boardCategoryId(bctemp).categoryName("0805").build();
////		System.out.println(bcvotemp);
//		boardcateRP.save(bcvotemp);
//
//		
//		BoardVO boardTemp = BoardVO.builder().category(bcvotemp).user(uservo).boardTitle("pageable sample 0805").build(); 
//		boardRP.save(boardTemp);
//		
//		
//		});
//	}
	
//	@Test
//	public void boardPageList() {
//		Pageable pageable = PageRequest.of(0, 4, Direction.DESC, "boardId");
//		Pageable pageableBeta = PageRequest.of(1, 4, Direction.DESC, "boardId");
//		//Page request [number: 0, size 10, sort: boardId: DESC]
//				
//		Page<BoardVO> result = boardRP.findAll(boardRP.makePredicate(null, null), pageable);
//		Page<BoardVO> result2 = boardRP.findAll(boardRP.makePredicate(null, null), pageableBeta);
//		
//		System.out.println("result: "+result.getPageable());
//		System.out.println("result2: "+result2.getPageable());
//
//		System.out.println("result1-----------------");
//		result.getContent().forEach(a->{
//			System.out.println("getContent: "+a);
//		});
//		
//		System.out.println("result2-----------------");
//		result2.getContent().forEach(a->{
//			System.out.println("getContent: "+a);
//		});
//		
////		result3.getContent().forEach(a->{
////			System.out.println("getContent: "+a);
////		});
//		
//	}
	
//	@Test
//	public void categoryNameInsertByuserID() {
//		
//		
////		Long teamID = 21L; //teams -> team_id
//		String userID = "0720"; //user_id
//		String categoryName = "sample0802beta";	
//		
//		List<Long>categoryID = boardcateRP.selectIDByname(categoryName);
//		
//		System.out.println(categoryID);
//		if(categoryID.isEmpty()) {
//			categoryNameInsert(categoryName, userID, boardcateRP);
//		}
//	}
//
//	private void categoryNameInsert(String categoryName, String userID, BoardCategoryRepositoryTestSu boardcateRP2) {
//		//BoardCategoryMultikey -> generateValue not work
//		long gnrTemp =  new Random().nextLong();
//		if(gnrTemp <0) {
//			gnrTemp = -1*gnrTemp;
//		}
//		long gnrValue = Long.valueOf(String.valueOf(gnrTemp).substring(0, 6));
////		System.out.println(gnrValue);
//		
//		UserVO uservo = userRP.findById(userID).get();
//		TeamVO teamvo =  teamRP.selectByUserID(uservo.getUserId());
//		System.out.println(teamvo);
//		
//		BoardCategoryMultikey bcMultikey = BoardCategoryMultikey.builder().categoryId(gnrValue).team(teamvo).build();
//		BoardCategoryVO bcTemp = BoardCategoryVO.builder().boardCategoryId(bcMultikey).categoryName(categoryName).build();
//		boardcateRP.save(bcTemp);
//	}

	
//	@Test
//	public void boardSelectByCategoryID() {
//		List<Long>categoryID = boardcateRP.selectIDByname("sample2022");
//		
//		List<BoardVO> boardList = new ArrayList<>();
//		categoryID.forEach(a->{
//			BoardVO temp = boardRP.selectBoardByID(a);
//			if(temp != null) {
//				boardList.add(temp);
//			}
//		});
//		
//		boardList.forEach(a->{
//			System.out.println(a);
//		});
//	}
	
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
