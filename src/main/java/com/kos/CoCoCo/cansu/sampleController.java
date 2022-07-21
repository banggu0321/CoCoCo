package com.kos.CoCoCo.cansu;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

@Controller
public class sampleController {
	
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
	
	@GetMapping("/replyDeleteSample")
	public String replyDelete(HttpServletRequest request) {
		
		String replyID = request.getParameter("rid");
//		System.out.println(replyID);
		
		replyRP.deleteById(Long.valueOf(replyID));
		
		return "redirect:/boardSample";
	}
	
	@GetMapping("/boardDelete")
	public String boardDelete(HttpServletRequest request) {
		
		String boardID = request.getParameter("bid");
		
		List<ReplyVO> rlist = replyRP.selectByboardID(Integer.valueOf(boardID));
		if(rlist.isEmpty())
		{
			boardRP.deleteById(Long.valueOf(boardID));			
		}
		
		return "redirect:/boardSample";
	}
	
	@PostMapping("/postReplyInsert")
	public String replyInsert(HttpServletRequest request) {
		
		String text = request.getParameter("replyText");		
		String boardID = request.getParameter("replyBid");
//		System.out.println(boardID);
//		System.out.println(text);
		
		UserVO uservo = userRP.findById("0720").get();  //log in
		BoardVO boardTemp = boardRP.findById(Long.valueOf(boardID)).get();  //selected board
		
		ReplyVO rvo = ReplyVO.builder().board(boardTemp).user(uservo).replyText(text).build();
		replyRP.save(rvo);
		
		return "redirect:/boardSample";
	}
	
	@GetMapping("/boardUDsample")
	public String boardUD(HttpServletRequest request, Model model) {
		
		//main list selected board get -> error
		String boardID = request.getParameter("id");
//		System.out.println(boardID);
		
		BoardVO bvo = boardRP.findById(Long.valueOf(boardID)).get();
		model.addAttribute("boardDetail", bvo);
		
		List<ReplyVO> rlist = replyRP.selectByboardID(Integer.valueOf(boardID));
		model.addAttribute("replyList", rlist);		
//		System.out.println(bvo.getBoardText());
		
		return "su/boardUpdateAndDelete";
	}
	
	@PostMapping("/postBoardUpdate")
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
		
		return "redirect:/boardSample";
	}

	@GetMapping("/boardSample")
	public String boardlist(Model model) {		
		List<BoardVO> bvo = (List<BoardVO>)boardRP.findAll();		
		model.addAttribute("board", bvo);	
		return "su/boardMain";
	}

	@GetMapping("/boardInsertSample")
	public String boardInsert() {
		return "su/boardInsert";
	}

	@PostMapping("/postBoardInsertSample")
	public String boardInsertPost(HttpServletRequest request) {

		System.out.println("title: "+request.getParameter("title"));
		System.out.println("content: "+request.getParameter("content"));

		String title = request.getParameter("title");
		String content =request.getParameter("content");

		makeBoardSample(title, content);

		return "redirect:/boardSample";
	}

	private void makeBoardSample(String title, String content) {
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
		String categoryName = "sample2022";
		BoardCategoryMultikey bctemp = BoardCategoryMultikey.builder().categoryId(gnrValue).team(teamvo).build();
		BoardCategoryVO bcvotemp = BoardCategoryVO.builder().boardCategoryId(bctemp).categoryName(categoryName).build();
		//		System.out.println(bcvotemp);
		boardcateRP.save(bcvotemp);

		BoardVO boardTemp = BoardVO.builder().category(bcvotemp).user(uservo).boardTitle(title).boardText(content).build(); 
		boardRP.save(boardTemp);
	}
}
