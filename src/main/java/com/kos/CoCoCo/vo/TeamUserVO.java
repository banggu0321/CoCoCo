package com.kos.CoCoCo.vo;

import java.sql.Timestamp;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
public class TeamUserVO {
	
	@EmbeddedId
	TeamUserMultikey teamUserId;
	
	@CreationTimestamp
	private Timestamp joinDate;
	
	private String userRole;
}
