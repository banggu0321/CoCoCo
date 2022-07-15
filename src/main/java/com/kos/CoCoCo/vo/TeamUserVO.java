package com.kos.CoCoCo.vo;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "teamUser")
@IdClass(TeamUserMultikey.class)
public class TeamUserVO {
	
	@Id
	@ManyToOne
	@JoinColumn(name = "teams_team_id")
	TeamVO team;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "users_user_id")
	UserVO user;
	
	@CreationTimestamp
	private Timestamp joinDate;
	
	private String userRole;
}
