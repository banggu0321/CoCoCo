package com.kos.CoCoCo.board;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kos.CoCoCo.cansu.test.BoardCategoryRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.PageVO;
import com.kos.CoCoCo.cansu.test.ReplyRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.TeamRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.UserRepositoryTestSu;
import com.kos.CoCoCo.vo.BoardCategoryMultikey;
import com.kos.CoCoCo.vo.BoardCategoryVO;
import com.kos.CoCoCo.vo.BoardVO;
import com.kos.CoCoCo.vo.ReplyVO;
import com.kos.CoCoCo.vo.TeamVO;

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
	BoardRepositoryT boardRP;
	
	//@Autowired
	//BoardCategoryRepositoryTestSu categoryRP;
	
	@Autowired
	BoardCategoryRepositoryT boardcateRP,categoryRP;
	
	@Autowired
	ReplyRepositoryTestSu replyRP;
	
	
	@GetMapping("/replyDeleteByID/{replyid}")
	public List<ReplyVO> deleteReply(@PathVariable String replyid, Principal principal){
		System.out.println("principal: "+principal.getName());
		System.out.println("reply id: "+replyid);
		
			
		ReplyVO reply = replyRP.findById(Long.valueOf(replyid)).get();  //selected reply
		System.out.println("reply.userid: "+reply.getUser().getUserId());
		String replyUser = reply.getUser().getUserId();
		
		if(replyUser.equals(principal.getName())) {
			replyRP.delete(reply);
		}			
		
		Long bno = reply.getBoard().getBoardId();
		BoardVO board = BoardVO.builder().boardId(bno).build();
		
		List<ReplyVO> replyList = replyRP.selectByboardID(board.getBoardId().intValue());
		
		return replyList;
	}
	
	@GetMapping("/boardList/fromMain")
	public List<BoardVO> boardListByPageable(PageVO vo, Model model){
		
//		public Pageable makePageable(int direction, String... props) {
//			Sort.Direction dir = direction == 0 ? Sort.Direction.DESC : Sort.Direction.ASC;
//			return PageRequest.of(this.page-1, this.size, dir, props);  //page-1 = 1-1;  //this.size = DEFAULT_SIZE;
//		}
		Pageable page = vo.makePageable(0, "boardId");
		Page<BoardVO>  result = boardRP.findAll(boardRP.makePredicate(null, null), page);
		return result.getContent();  //board list
	}
	
	@GetMapping("/getBoardPage/{pageNumber}")
	public List<BoardVO> boardlistByPageNum(@PathVariable String pageNumber, Model model, Principal principal){
		
		System.out.println("page number: "+pageNumber);
		
		System.out.println("principal.getName():" + principal.getName());
		String userID = principal.getName();  //login -> id
		System.out.println("page number: "+pageNumber);
		
		PageVO pageTemp = new PageVO();
		int pageSize = pageTemp.getSize();
		
		//error
		Pageable pageable = PageRequest.of(Integer.valueOf(pageNumber)-1, pageSize, Direction.DESC, "boardId");
		Page<BoardVO> result = boardRP.findAll(boardRP.makePredicate(null, null), pageable);
		
		List<BoardVO> boardList = result.getContent();
		boardList.forEach(a->{
			System.out.println(a);
		});
		
		return boardList;
	}
	
//	@GetMapping("/boardList/insertName/{name}/{teamid}")
//	public List<BoardVO> categoryNameInsertFromBLBeta(@PathVariable String teamid, @PathVariable String name,Model model, Principal principal) {
//		
//		System.out.println("principal.getName():" + principal.getName());
//		String userID = principal.getName(); //user_id
//		String teamID = teamid;
//		
//		System.out.println("team id: "+teamID);
//		System.out.println("category name: "+name);
//		
//		if(name==null) {
//			return (List<BoardVO>)boardRP.findAll();
//		}
//		
//		List<Long>categoryID = boardcateRP.selectIDByname(name);
//		if(categoryID.isEmpty()) {
//			categoryNameInsert(teamID, name, userID, boardcateRP);
//		}
//		
//		List<BoardVO> boardList = new ArrayList<>();
//		categoryID.forEach(a->{
//			BoardVO temp = boardRP.selectBoardByID(a);
//			if(temp != null) {
//				boardList.add(temp);
//			}
//		});
//		
//		return boardList;
//	}
	
	@GetMapping("/boardList/insertName/{name}/{teamid}")
	public BoardCategoryVO categoryNameInsertFromBLBeta(@PathVariable Long teamid, @PathVariable String name,Model model, HttpSession session) {
		List<Long>categoryID = boardcateRP.selectIDByname(name);
		
		BoardCategoryVO bc = null;
		if(categoryID.isEmpty()) {
			bc = categoryNameInsert(teamid, name, boardcateRP);
		}
		
		session.setAttribute("cateName", boardcateRP.selectByTeam(teamid));
		return bc;
	}
	
	@GetMapping("/boardListBycategory/{name}")
	public List<BoardVO> boardlistByCategory( @PathVariable String name,Model model) {
		
		if(name==null) {
			return (List<BoardVO>)boardRP.findAll();
		}
		
		Pageable pageable = PageRequest.of(0, 5, Sort.by(Direction.DESC, "board_id"));
		List<Long>categoryID = boardcateRP.selectIDByname(name);
//		Page<BoardVO> result = boardRP.selectBoardByIDbeta(categoryID, pageable);
		List<BoardVO> boardList = boardRP.selectBoardByIDbeta(categoryID, pageable);
		boardList.forEach(a->{
			System.out.println(a);
		});
		
		model.addAttribute("category", name);
		
		return boardList;
	}
	
//	@GetMapping("/boardList/{name}")
//	public List<BoardVO> boardlistbeta(@PathVariable String name,Model model) {
//		
//		if(name==null) {
//			return (List<BoardVO>)boardRP.findAll();
//		}
//		
//		List<Long>categoryID = boardcateRP.selectIDByname(name);
//		List<BoardVO> boardList = new ArrayList<>();
//		categoryID.forEach(a->{
//			BoardVO temp = boardRP.selectBoardByID(a);
//			if(temp != null) {
//				boardList.add(temp);
//			}
//		});
//		
//		return boardList;
//	}
	
	@GetMapping("/boardList/{categoryId}")
	public List<BoardVO> boardlistbeta(@PathVariable Long categoryId,Model model) {
		if(categoryId==null) {
			return (List<BoardVO>)boardRP.findAll();
		}
		
		return boardRP.selectBoardByCategory(categoryId);
	}
	
	@GetMapping("/categoryName/{teamid}")
	public List<String> categoryName(@PathVariable String teamid, Model model){		
		Long result = Long.valueOf(teamid);
		System.out.println("result: "+result);
		
		List<BoardCategoryVO> ctList = categoryRP.selectByTeam(result);		
		List<String> listResult = new ArrayList<>();
		for(BoardCategoryVO temp: ctList) {
//			System.out.println("temp: "+temp);
			listResult.add(temp.getCategoryName());
		}
		return listResult;
	}
	
	@GetMapping("/categoryList")
	public List<BoardCategoryVO> categoryList(Model model){
		return (List<BoardCategoryVO>)categoryRP.findAll();
	}

	
	//changed
	@GetMapping("/{bno}")
	public List<ReplyVO> replyList(@PathVariable Long bno){
		BoardVO board = BoardVO.builder().boardId(bno).build();
		
		return reply.selectByboardID(board.getBoardId().intValue());
	}
	
	@GetMapping("/boardList")
	public List<BoardVO> boardlist(Model model) {			
		return (List<BoardVO>)boardRP.findAll();
	}
	
	private BoardCategoryVO categoryNameInsert(Long teamID, String categoryName, BoardCategoryRepositoryT boardcateRP2) {
		//BoardCategoryMultikey -> generateValue not work
		long gnrTemp =  new Random().nextLong();
		if(gnrTemp <0) {
			gnrTemp = -1*gnrTemp;
		}
		long gnrValue = Long.valueOf(String.valueOf(gnrTemp).substring(0, 6));
//		System.out.println(gnrValue);
		
		TeamVO teamvo = teamRP.findById(teamID).get();
		System.out.println(teamvo);
		
		BoardCategoryMultikey bcMultikey = BoardCategoryMultikey.builder().categoryId(gnrValue).team(teamvo).build();
		BoardCategoryVO bcTemp = BoardCategoryVO.builder().boardCategoryId(bcMultikey).categoryName(categoryName).build();
		BoardCategoryVO bc =  boardcateRP.save(bcTemp);
		return bc;
	}
}
