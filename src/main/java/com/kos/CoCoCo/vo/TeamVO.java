package com.kos.CoCoCo.vo;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "teams")
public class TeamVO {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long teamId;
	
	private String teamName;
	
	private String teamInfo;
	
	//만든사람
	@ManyToOne
	@JoinColumn(name = "users_user_id")
	UserVO user;
	
	@CreationTimestamp
	private Timestamp createDate;
	
	private String inviteCode;

	
	
}
