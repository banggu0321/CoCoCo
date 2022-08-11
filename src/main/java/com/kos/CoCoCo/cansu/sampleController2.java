package com.kos.CoCoCo.cansu;

import java.io.IOException;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kos.CoCoCo.cansu.test.BoardCategoryRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.BoardRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.PageMaker;
import com.kos.CoCoCo.cansu.test.PageVO;
import com.kos.CoCoCo.cansu.test.ReplyRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.TeamRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.UserRepositoryTestSu;
import com.kos.CoCoCo.ja0.awsS3.AwsS3;
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
	
	@Autowired
	boardUDFileService boardService;
	
	@Autowired
	AwsS3 awsS3;

	@GetMapping("/boardFileDown/{bno}")
	public ResponseEntity<byte[]> boardDownload(@PathVariable Long bno) throws IOException{
		String dir = "uploads/boardFile/";
		BoardVO board = boardRP.findById(bno).get();
		System.out.println(board.getBoardFile());
		
		return awsS3.download(dir, board.getBoardFile().substring(72));
	}
	
	@RequestMapping(value = "/boardLSearch/{key}/{value}", method = RequestMethod.GET)
	public String selectBoardByobj(@PathVariable String key, @PathVariable String value, HttpSession session, Model model){
		
		String rIndex = key;
		String rValue = value;
		Long teamId = (Long) session.getAttribute("teamId");
		
		System.out.println("index: "+rIndex);
		System.out.println("value: "+rValue);
		System.out.println("team id: "+teamId);
		
		List<BoardVO> boardList = new ArrayList<>();
		switch(rIndex) {
		case "t":
			boardList = searchByTitle(teamId, rValue);
			break;
		case "w":
			boardList = searchByWriter(teamId,rValue);
			break;
		case "c":
			boardList = searchByContent(teamId,rValue);
			break;
		case "d":
			boardList = searchByDay(teamId,rValue);
			break;	
		}

		model.addAttribute("boardList", boardList);
		
//		Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "board_id");
//		Page<BoardVO> result = new PageImpl<BoardVO>(boardList, pageable, boardList.size());
//		//PageMaker<BoardVO> resultPage = new PageMaker<>(result);
//					
//		model.addAttribute("result", new PageMaker(result));		
		return "su/thymeleaf/boardMainBeta";
	}
	
	
	private List<BoardVO> searchByDay(Long teamId, String rValue) {
		List<BoardVO> result = new ArrayList<>();
		List<BoardVO> boards = boardRP.selectBoardByteam(teamId);	
//		System.out.println("boards size: "+boards.size());
		
		String dateValue = "20220101";
		if(rValue.length() == 4) {
			dateValue = "2022"+rValue;
		} 		
		
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatr = new SimpleDateFormat("yyyyMMdd");
		try {
			Date date = formatr.parse(dateValue);
			
			for(BoardVO temp:boards) {
//				System.out.println(temp.getBoardRegDate());
				if(temp.getBoardRegDate().after(date) ) {
						result.add(temp);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;
	}


	private List<BoardVO> searchByContent(Long teamId, String rValue) {
		

		List<BoardVO> boards = boardRP.selectBoardByteam(teamId);	
		System.out.println("boards size: "+boards.size());
		
		List<BoardVO> result = new ArrayList<>();
		for(BoardVO temp:boards) {
			System.out.println(temp.getBoardText());
			if((temp.getBoardText() != null)&&(temp.getBoardText().contains(rValue))) {
					result.add(temp);
			}
		}
		return result;
	}


	private List<BoardVO> searchByWriter(Long teamId, String rValue) {
		
		List<BoardVO> boards = boardRP.selectBoardByteam(teamId);	
		System.out.println("boards size: "+boards.size());
		
		List<BoardVO> result = new ArrayList<>();
		for(BoardVO temp:boards) {
			if(temp.getUser().getName().equals(rValue)) {
				result.add(temp);
			}
		}
		return result;
	}


	private List<BoardVO> searchByTitle(Long team, String rValue) {
		Long teamId = team;
		
		List<BoardVO> boards = boardRP.selectBoardByteam(teamId);	
		System.out.println("boards size: "+boards.size());
		
		List<BoardVO> result = new ArrayList<>();
		for(BoardVO temp:boards) {
			if(temp.getBoardTitle().contains(rValue)) {
				result.add(temp);
			}
		}
		return result;		
	}


	@GetMapping("/getNext/{pageNumber}")
	public String boardMainNavByPageNum(@PathVariable String pageNumber, Model model){
		
		PageVO voTemp = new PageVO();
		

		Pageable page = PageRequest.of(Integer.valueOf(pageNumber), voTemp.getSize(), Direction.DESC, "boardId");
		Page<BoardVO>  result = boardRP.findAll(boardRP.makePredicate(null, null), page);
		
		model.addAttribute("result", new PageMaker(result));
		model.addAttribute("boardList", result.getContent());
		
		System.out.println("pageNumber: "+pageNumber);
		System.out.println("page size: "+voTemp.getSize());
		
		return "su/thymeleaf/boardMain";
	}
	
	@GetMapping("/boardSampleBeta/{name}")
	public String boardlistByCategory(@PathVariable String name,Model model) {
		
		if(name==null) {
			return "/boardSampleBeta";
		}
		
		Pageable pageable = PageRequest.of(0, 5, Sort.by(Direction.DESC, "board_id"));
		List<Long> categoryID = boardcateRP.selectIDByname(name);
		List<BoardVO> boardList = boardRP.selectBoardByIDbeta(categoryID, pageable);
		
		Page<BoardVO> result = new PageImpl<BoardVO>(boardList, PageRequest.of(0, 5), boardList.size());
		
		System.out.println("category name: "+name);
		
//		System.out.println("result: "+ new PageMaker(result));
		
		
		model.addAttribute("result", new PageMaker(result));
		model.addAttribute("boardList", boardList);
		
		return "su/thymeleaf/boardMain";
	}
	
	@GetMapping("/boardSampleBeta")
	public String boardlist(HttpSession session, PageVO vo, Model model) {	
		
//		Pageable page = vo.makePageable(0, "boardId");
//		Page<BoardVO>  result = boardRP.findAll(boardRP.makePredicate(null, null), page);
//		
//		model.addAttribute("result", new PageMaker(result));		
//		model.addAttribute("boardList", result.getContent());
//		
//		return "su/thymeleaf/boardMain";

		Long teamId = (Long) session.getAttribute("teamId");
		List<BoardVO> boards = boardRP.selectBoardByteam(teamId);	
		System.out.println("teamId: "+teamId);
		
		Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "board_id");
		Page<BoardVO> result = new PageImpl<BoardVO>(boards, pageable, boards.size());
			
		List<BoardVO> boardList = boardRP.selectBoardByteamBeta(teamId, pageable);
//		PageMaker<BoardVO> resultPage = new PageMaker<>(result);
		
		model.addAttribute("result", new PageMaker(result));
		model.addAttribute("boardList", boardList);
		return "su/thymeleaf/boardMain";
	}
		
	//void - error, insert는 controller.method가 필요
	@PostMapping("/postReplyInsertBeta")
	public String replyInsertBeta(HttpServletRequest request, Principal principal) {
		
		String text = request.getParameter("replyText");		
		String boardID = request.getParameter("replyBid");
//		System.out.println(boardID);
//		System.out.println(text);
		
		UserVO uservo = userRP.findById(principal.getName()).get();  //log in
		BoardVO boardTemp = boardRP.findById(Long.valueOf(boardID)).get();  //selected board
		
		ReplyVO rvo = ReplyVO.builder().board(boardTemp).user(uservo).replyText(text).build();
		replyRP.save(rvo);
		
		return "redirect:/boardUDsampleBeta?id="+boardID;
	}
	
	@GetMapping("/boardUDsampleBeta")
	public String boardUDBeta(HttpServletRequest request, Model model, Principal principal) {
		
		String userid = principal.getName();
		String boardID = request.getParameter("id");
		System.out.println("board id: "+boardID);
		
//		model.addAttribute("replyInsertID", userid);
//		System.out.println(boardID);
		
		BoardVO bvo = boardRP.findById(Long.valueOf(boardID)).get();
		model.addAttribute("boardDetail", bvo);
		
		List<ReplyVO> rlist = replyRP.selectByboardID(Integer.valueOf(boardID));
		model.addAttribute("replyList", rlist);
		
		System.out.println("userid: "+userid);
		System.out.println("board.user.id: "+bvo.getUser().getUserId());
		if(bvo.getUser().getUserId().equals(userid)) {
			System.out.println("true");
			return "su/thymeleaf/boardUpdateAndDeleteBeta";
		} else{
			System.out.println("false");
			return "su/thymeleaf/boardDisableUpdate";
		}
	}
	
	@GetMapping("/boardInsertSample2/{name}/{teamid}")
	public String boardInsertBeta(@PathVariable String teamid, @PathVariable String name, Model model) {
		model.addAttribute("categoryName", name);
		model.addAttribute("teamid", teamid);
		return "su/thymeleaf/boardInsert";
	}
	
	@GetMapping("/boardDeleteBeta")
	public String boardDelete(HttpServletRequest request) {
		
		String boardID = request.getParameter("bid");
		
		List<ReplyVO> rlist = replyRP.selectByboardID(Integer.valueOf(boardID));
		if(rlist.isEmpty())
		{
			boardRP.deleteById(Long.valueOf(boardID));			
		}else if(!rlist.isEmpty()) {
			for(ReplyVO temp: rlist) {
				replyRP.deleteById(temp.getReplyId());
			}
			boardRP.deleteById(Long.valueOf(boardID));
		}
		return "redirect:/boardSampleBeta";
	}
	
	@PostMapping("/postBoardUpdateBeta")
	public String boardUpdate(HttpServletRequest request, @RequestParam("insertFile2") MultipartFile insertFile) throws IllegalStateException, IOException{
		
		String title = request.getParameter("boardTitle");
		String text = request.getParameter("boardText");
		String id = request.getParameter("boardID");
		
		System.out.println("post title: "+title);
		System.out.println("post text: "+text);
		System.out.println("post id: "+id);
		System.out.println("insertFile: "+insertFile);

		BoardVO bvo = boardRP.findById(Long.valueOf(id)).get();
		
		System.out.println("insert"+ insertFile);
		
		if(insertFile.isEmpty()) {
			System.out.println("변경없음");
			
		}else {
			awsS3.delete(bvo.getBoardFile());
			
			String fileName = awsS3.upload(insertFile, "uploads/boardFile/");
			System.out.println("aws - file name: "+fileName);
			
			bvo.setBoardFile(fileName);
		}
		
		bvo.setBoardTitle(title);
		bvo.setBoardText(text); 
		boardRP.save(bvo);
		
		return "redirect:/boardSampleBeta";
	}

	@PostMapping("/postBoardInsertSample2")
	public String boardInsertPostBeta(HttpServletRequest request, Principal principal, MultipartFile[] insertFile) throws IllegalStateException, IOException {

		String title = request.getParameter("title");
		String content =request.getParameter("content");
		String category = request.getParameter("category");
		String userID = principal.getName();
		String teamid = request.getParameter("teamid");
		
		System.out.println("title: "+title);
		System.out.println("content: "+content);
		System.out.println("category: "+category);
		System.out.println("userID: "+userID);
		System.out.println("team id: "+teamid);
		
		makeBoardSample(teamid, title, content,category,userID, insertFile);

		return "redirect:/boardSampleBeta";
	}
	private void makeBoardSample(String teamid, String title, String content, String category, String userID, MultipartFile[] insertFile) throws IllegalStateException, IOException {
		long gnrTemp =  new Random().nextLong();
		if(gnrTemp <0) {
			gnrTemp = -1*gnrTemp;
		}
		long gnrValue = Long.valueOf(String.valueOf(gnrTemp).substring(0, 6));
		//		System.out.println(gnrValue);

		//(log in info)user->team
		UserVO uservo = userRP.findById(userID).get();  
//		TeamVO teamvo =  teamRP.selectByUserID(uservo.getUserId());
		TeamVO teamvo = teamRP.findById(Long.valueOf(teamid)).get();
		System.out.println(teamvo);

		//categoryName 
		String categoryName = category;
		BoardCategoryMultikey bctemp = BoardCategoryMultikey.builder().categoryId(gnrValue).team(teamvo).build();
		BoardCategoryVO bcvotemp = BoardCategoryVO.builder().boardCategoryId(bctemp).categoryName(categoryName).build();
		//		System.out.println(bcvotemp);
		boardcateRP.save(bcvotemp);

		int checkNum =0;
		for(MultipartFile temp: insertFile) {
			if(temp.getSize() != 0) {
				checkNum += 1;
			}
		}
		System.out.println("checkNum: "+checkNum);
		
		BoardVO boardTemp=null;
		if(checkNum >0) {
//			List<String> boardFileName = boardService.uploadFile(insertFile);
			
			//cloud file upload
			String fileName = awsS3.upload(insertFile[0], "uploads/boardFile/");
			System.out.println("aws - file name: "+fileName);
			
			boardTemp = BoardVO.builder().category(bcvotemp).user(uservo).boardTitle(title).boardText(content).boardFile(fileName).build(); 
//			boardTemp = BoardVO.builder().category(bcvotemp).user(uservo).boardTitle(title).boardText(content).boardFile(boardFileName.get(0)).build(); 
		} else{
			boardTemp = BoardVO.builder().category(bcvotemp).user(uservo).boardTitle(title).boardText(content).build(); 
		}
		boardRP.save(boardTemp);
	}
}
