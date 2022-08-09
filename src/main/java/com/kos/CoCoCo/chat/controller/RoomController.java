package com.kos.CoCoCo.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kos.CoCoCo.chat.dto.ChatRoomDTO;
import com.kos.CoCoCo.chat.repository.ChatRoomRepository;
import com.kos.CoCoCo.chat.repository.MessageRepository;
import com.kos.CoCoCo.chat.repository.RoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Log4j2
public class RoomController {

	@Autowired
    private  RoomRepository roomRepo;
	
	@Autowired
	private MessageRepository mRepo;

    //채팅방 목록 조회
    @GetMapping(value = "/rooms")
    public ModelAndView rooms(){

        log.info("# All Chat Rooms");
        
 
        ModelAndView mv = new ModelAndView("chat/rooms");

        mv.addObject("list", roomRepo.findAll());

        return mv;
    }

    //채팅방 개설
//    @PostMapping(value = "/room")
//    public String create(@RequestParam String name, RedirectAttributes rttr){
//
//        log.info("# Create Chat Room , name: " + name);
//        ChatRoomDTO room = ChatRoomDTO.create(name);
//        roomRepo.save(room);
//        rttr.addFlashAttribute("roomName",name);
//        return "redirect:/chat/rooms";
//    }

    //채팅방 조회
    @GetMapping("/room")
    public void getRoom(Long teamId, Model model){

        log.info("# get Chat Room, teamID : " + teamId);
        model.addAttribute("team", roomRepo.findById(teamId).get());
        model.addAttribute("chatlist",mRepo.findByTeamIdOrderById(teamId));
        
    }
}