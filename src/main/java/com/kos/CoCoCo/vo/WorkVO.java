package com.kos.CoCoCo.vo;

import java.sql.Date;
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "works")
public class WorkVO {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long workId;
	
	@ManyToOne
	@JoinColumn(name = "teams_team_id")
	TeamVO team;
	
	private String workTitle;
	
	private String workText;
	
	private Date workStart;
	
	private Date workEnd;
	
	private String workStatus;

}
