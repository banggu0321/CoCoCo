package com.kos.CoCoCo.cansu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kos.CoCoCo.cansu.test.BoardCategoryRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.BoardRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.ReplyRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.TeamRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.UserRepositoryTestSu;
import com.kos.CoCoCo.vo.BoardCategoryMultikey;
import com.kos.CoCoCo.vo.BoardCategoryVO;
import com.kos.CoCoCo.vo.BoardVO;
import com.kos.CoCoCo.vo.ReplyVO;
import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.UserVO;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/BOARDrest/*")
public class sampleRESTController {
	
	@Autowired
	UserRepositoryTestSu userRP;
	
	@Autowired
	TeamRepositoryTestSu teamRP;
	
	@Autowired
	ReplyRepositoryTestSu reply;
	
	@Autowired
	BoardRepositoryTestSu boardRP;
	
	@Autowired
	BoardCategoryRepositoryTestSu categoryRP;
	
	@Autowired
	BoardCategoryRepositoryTestSu boardcateRP;
	
	@GetMapping("/getBoardPage/{pageNumber}")
	public List<BoardVO> boardlistByPageNum(@PathVariable String pageNumber, Model model){
		String userID = "0720";  //login -> id
		System.out.println("page number: "+pageNumber);
		
		Pageable pageable = PageRequest.of(Integer.valueOf(pageNumber)-1, 4, Direction.DESC, "boardId");
		Page<BoardVO> result = boardRP.findAll(boardRP.makePredicate(null, null), pageable);
		
		List<BoardVO> boardList = result.getContent();
		System.out.println("board list: "+boardList);
		
		return boardList;
	}
	
	@GetMapping("/boardList/insertName/{name}")
	public List<BoardVO> categoryNameInsertFromBLBeta(@PathVariable String name,Model model) {
		String userID = "0720"; //user_id
		System.out.println("category name: "+name);
		
		if(name==null) {
			return (List<BoardVO>)boardRP.findAll();
		}
		
		List<Long>categoryID = boardcateRP.selectIDByname(name);
		if(categoryID.isEmpty()) {
			categoryNameInsert(name, userID, boardcateRP);
		}
		
		List<BoardVO> boardList = new ArrayList<>();
		categoryID.forEach(a->{
			BoardVO temp = boardRP.selectBoardByID(a);
			if(temp != null) {
				boardList.add(temp);
			}
		});
		
		return boardList;
	}
	
	@GetMapping("/boardList/{name}")
	public List<BoardVO> boardlistbeta(@PathVariable String name,Model model) {
		
		if(name==null) {
			return (List<BoardVO>)boardRP.findAll();
		}
		
		List<Long>categoryID = boardcateRP.selectIDByname(name);
		List<BoardVO> boardList = new ArrayList<>();
		categoryID.forEach(a->{
			BoardVO temp = boardRP.selectBoardByID(a);
			if(temp != null) {
				boardList.add(temp);
			}
		});
		
		return boardList;
	}
	
	@GetMapping("/categoryName")
	public List<String> categoryName(Model model){
		return (List<String>)categoryRP.selectAllCategoryName();
	}
	
	@GetMapping("/categoryList")
	public List<BoardCategoryVO> categoryList(Model model){
		return (List<BoardCategoryVO>)categoryRP.findAll();
	}

	@GetMapping("/{bno}")
	public List<ReplyVO> replyList(@PathVariable Long bno){
		BoardVO board = BoardVO.builder().boardId(bno).build();
		
		return reply.selectByboardID(board.getBoardId().intValue());
	}
	
	@GetMapping("/boardList")
	public List<BoardVO> boardlist(Model model) {			
		return (List<BoardVO>)boardRP.findAll();
	}
	
	private void categoryNameInsert(String categoryName, String userID, BoardCategoryRepositoryTestSu boardcateRP2) {
		//BoardCategoryMultikey -> generateValue not work
		long gnrTemp =  new Random().nextLong();
		if(gnrTemp <0) {
			gnrTemp = -1*gnrTemp;
		}
		long gnrValue = Long.valueOf(String.valueOf(gnrTemp).substring(0, 6));
//		System.out.println(gnrValue);
		
		UserVO uservo = userRP.findById(userID).get();
		TeamVO teamvo =  teamRP.selectByUserID(uservo.getUserId());
		System.out.println(teamvo);
		
		BoardCategoryMultikey bcMultikey = BoardCategoryMultikey.builder().categoryId(gnrValue).team(teamvo).build();
		BoardCategoryVO bcTemp = BoardCategoryVO.builder().boardCategoryId(bcMultikey).categoryName(categoryName).build();
		boardcateRP.save(bcTemp);
	}
}
