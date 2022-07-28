package com.kos.CoCoCo.cansu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kos.CoCoCo.cansu.test.BoardRepositoryTestSu;
import com.kos.CoCoCo.cansu.test.ReplyRepositoryTestSu;
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
