package com.kos.CoCoCo.cansu;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kos.CoCoCo.cansu.test.BoardCategoryRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.BoardRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.ReplyRepositoryTestSu;
import com.kos.CoCoCo.vo.BoardCategoryVO;
import com.kos.CoCoCo.vo.BoardVO;
import com.kos.CoCoCo.vo.ReplyVO;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/BOARDrest/*")
public class sampleRESTController {
	
	@Autowired
	ReplyRepositoryTestSu reply;
	
	@Autowired
	BoardRepositoryTestSu boardRP;
	
	@Autowired
	BoardCategoryRepositoryTestSu categoryRP;
	
	@Autowired
	BoardCategoryRepositoryTestSu boardcateRP;
	
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
}
