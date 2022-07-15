package com.kos.CoCoCo.vo;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chatting")
public class ChattingVO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long chatId;
	
	private Long teamId;
	
	private String userId;
	
	private String chatText;
	
	@CreationTimestamp
	private Timestamp chatSendTime;
}
