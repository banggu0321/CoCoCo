package com.kos.CoCoCo.chat.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.chat.dto.ChatMessageDTO;

public interface MessageRepository extends CrudRepository<ChatMessageDTO, Long>{
	
	List<ChatMessageDTO> findByRoomId(String roomId);

}
