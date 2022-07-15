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
@Table(name = "vacation")
public class VacationVO {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long vacationId;
	
	private String userId;
	
	private Date vacationStart;
	
	private Date vacationEnd;
}
