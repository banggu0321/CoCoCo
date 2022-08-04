package com.kos.CoCoCo.chat.repository;

import java.util.Map;

import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.chat.dto.ChatRoomDTO;

public interface RoomRepository extends CrudRepository<ChatRoomDTO, String>{
   //findAll()
}
