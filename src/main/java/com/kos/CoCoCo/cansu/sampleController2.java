package com.kos.CoCoCo.cansu;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.kos.CoCoCo.cansu.test.BoardCategoryRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.BoardRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.PageMaker;
import com.kos.CoCoCo.cansu.test.PageVO;
import com.kos.CoCoCo.cansu.test.ReplyRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.TeamRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.UserRepositoryTestSu;
import com.kos.CoCoCo.vo.BoardCategoryMultikey;
import com.kos.CoCoCo.vo.BoardCategoryVO;
import com.kos.CoCoCo.vo.BoardVO;
import com.kos.CoCoCo.vo.ReplyVO;
import com.kos.CoCoCo.vo.TeamVO;
import com.kos.CoCoCo.vo.UserVO;

@Controller
public class sampleController2 {
	
	@Autowired
	ReplyRepositoryTestSu replyRP;

	@Autowired
	BoardRepositoryTestSu boardRP;

	@Autowired
	UserRepositoryTestSu userRP;

	@Autowired
	TeamRepositoryTestSu teamRP;
	
	@Autowired
	BoardCategoryRepositoryTestSu boardcateRP;
	
//	@GetMapping("/boardSampleBeta")
//	public String boardlist() {			
//		return "su/thymeleaf/boardMain";
//	}
	
	@GetMapping("/boardSampleBeta")
	public String boardlist(PageVO vo, Model model) {	
		
		Pageable page = vo.makePageable(0, "boardId");
		Page<BoardVO>  result = boardRP.findAll(boardRP.makePredicate(null, null), page);
		
		model.addAttribute("result", new PageMaker(result));
		
		
		System.out.println("result.getSize: "+result.getSize());
		System.out.println("result.getTotalPage: "+result.getTotalPages());
		System.out.println("result.getTotalElements: "+result.getTotalElements());
		System.out.println("result.nextPageable: "+result.nextPageable());
		
		System.out.println("result.getPageable: "+ result.getPageable());
		System.out.println("result.getPageable.previoudOrFirst: "+result.getPageable().previousOrFirst());
		System.out.println("result.getPageable.getPageNumber: "+result.getPageable().getPageNumber());
		
		return "su/thymeleaf/boardMain";
	}
		
	//void - error, insert는 controller.method가 필요
	@PostMapping("/postReplyInsertBeta")
	public String replyInsertBeta(HttpServletRequest request) {
		
		String text = request.getParameter("replyText");		
		String boardID = request.getParameter("replyBid");
//		System.out.println(boardID);
//		System.out.println(text);
		
		UserVO uservo = userRP.findById("0720").get();  //log in
		BoardVO boardTemp = boardRP.findById(Long.valueOf(boardID)).get();  //selected board
		
		ReplyVO rvo = ReplyVO.builder().board(boardTemp).user(uservo).replyText(text).build();
		replyRP.save(rvo);
		
		return "redirect:/boardUDsampleBeta?id="+boardID;
	}
	
	@GetMapping("/boardUDsampleBeta")
	public String boardUDBeta(HttpServletRequest request, Model model) {
		
		//main list selected board get -> error
		String boardID = request.getParameter("id");
//		System.out.println(boardID);
		
		BoardVO bvo = boardRP.findById(Long.valueOf(boardID)).get();
		model.addAttribute("boardDetail", bvo);
		
		List<ReplyVO> rlist = replyRP.selectByboardID(Integer.valueOf(boardID));
		model.addAttribute("replyList", rlist);		
//		System.out.println(bvo.getBoardText());
		
		return "su/thymeleaf/boardUpdateAndDeleteBeta";
	}
	
	@GetMapping("/boardInsertSample2/{name}")
	public String boardInsertBeta(@PathVariable String name, Model model) {
		model.addAttribute("categoryName", name);
		return "su/thymeleaf/boardInsert";
	}
	
	@GetMapping("/boardDeleteBeta")
	public String boardDelete(HttpServletRequest request) {
		
		String boardID = request.getParameter("bid");
		
		List<ReplyVO> rlist = replyRP.selectByboardID(Integer.valueOf(boardID));
		if(rlist.isEmpty())
		{
			boardRP.deleteById(Long.valueOf(boardID));			
		}
		
		return "redirect:/boardSampleBeta";
	}
	
	@PostMapping("/postBoardUpdateBeta")
	public String boardUpdate(HttpServletRequest request){
		
		String title = request.getParameter("boardTitle");
		String text = request.getParameter("boardText");
		String id = request.getParameter("boardID");
		
//		System.out.println("post title: "+title);
//		System.out.println("post text: "+text);
//		System.out.println("post id: "+id);
		
		BoardVO bvo = boardRP.findById(Long.valueOf(id)).get();
		bvo.setBoardTitle(title);
		bvo.setBoardText(text);
		boardRP.save(bvo);
		
		return "redirect:/boardSampleBeta";
	}

	@PostMapping("/postBoardInsertSample2")
	public String boardInsertPostBeta(HttpServletRequest request) {

//		System.out.println("title: "+request.getParameter("title"));
//		System.out.println("content: "+request.getParameter("content"));
//		System.out.println("category name: "+request.getParameter("category"));

		String title = request.getParameter("title");
		String content =request.getParameter("content");
		String category = request.getParameter("category");

		makeBoardSample(title, content,category);

		return "redirect:/boardSampleBeta";
	}

	private void makeBoardSample(String title, String content, String category) {
		long gnrTemp =  new Random().nextLong();
		if(gnrTemp <0) {
			gnrTemp = -1*gnrTemp;
		}
		long gnrValue = Long.valueOf(String.valueOf(gnrTemp).substring(0, 6));
		//		System.out.println(gnrValue);

		//(log in info)user->team
		UserVO uservo = userRP.findById("0720").get();  
		TeamVO teamvo =  teamRP.selectByUserID(uservo.getUserId());
		System.out.println(teamvo);

		//categoryName 
		String categoryName = category;
		BoardCategoryMultikey bctemp = BoardCategoryMultikey.builder().categoryId(gnrValue).team(teamvo).build();
		BoardCategoryVO bcvotemp = BoardCategoryVO.builder().boardCategoryId(bctemp).categoryName(categoryName).build();
		//		System.out.println(bcvotemp);
		boardcateRP.save(bcvotemp);

		BoardVO boardTemp = BoardVO.builder().category(bcvotemp).user(uservo).boardTitle(title).boardText(content).build(); 
		boardRP.save(boardTemp);
	}
}
