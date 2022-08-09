package com.kos.CoCoCo.chat.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import groovy.transform.ToString;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Message")
public class ChatMessageDTO {
	
	@Id
	@GeneratedValue
	@Column(name = "chat_id")
	private Long id;
	
    private Long teamId;
	
    private String writer;

    private String message;
    
    private String KoreanName;

	@Override
	public String toString() {
		return "ChatMessageDTO [id=" + id + ", roomId=" + teamId + ", writer=" + writer + ", message=" + message + "]";
	}
   
    
    
    
    
}