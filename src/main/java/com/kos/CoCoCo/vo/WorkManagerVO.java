package com.kos.CoCoCo.vo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
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
@Table(name = "workManager")
@IdClass(WorkManagerMultikey.class)
public class WorkManagerVO {

	@Id
	private Long workId;
	
	@Id
	private String userId;
}
