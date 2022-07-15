package com.kos.CoCoCo.vo;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	private Long work_id;
	
	private Long team_id;
	
	private String work_title;
	
	private String work_text;
	
	private Date work_start;
	
	private Date work_end;
	
	private String work_status;
}
