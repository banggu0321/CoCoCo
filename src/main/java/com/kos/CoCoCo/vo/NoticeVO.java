package com.kos.CoCoCo.vo;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notice")
public class NoticeVO {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long noticeId;
	
	@ManyToOne
	@JoinColumn(name = "teams_team_id")
	TeamVO team;
	
	private String noticeTitle;
	
	private String noticeText;
	
	@ManyToOne
	@JoinColumn(name = "users_user_id")
	UserVO user;
	
	@CreationTimestamp
	private Timestamp noticeRegDate;
	
	@UpdateTimestamp
	private Timestamp noticeUpdate;
	
	private String fixedYN;
}
