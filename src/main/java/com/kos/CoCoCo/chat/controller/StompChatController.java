package com.kos.CoCoCo.chat.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kos.CoCoCo.chat.dto.ChatMessageDTO;
import com.kos.CoCoCo.chat.repository.MessageRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달
    
    @Autowired
    private MessageRepository mRepo;

    //Client가 SEND할 수 있는 경로
    //stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    //"/pub/chat/enter"
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDTO message, String data){
        message.setMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDTO message){
    	//ChatMessageDTO chat = ChatMessageDTO.createChat(message.getRoomId(),message.getWriter(),message.getMessage());
    	mRepo.save(message);
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
    
    @GetMapping(value = "/chat/del")
    //@ResponseBody
    public String chatdel(Long chatId,String roomId) {
    	System.out.println("삭제 : " + chatId);
    	mRepo.deleteById(chatId);
    	return "redirect:/chat/room?roomId="+roomId;
    	//return "ok";
    }
}