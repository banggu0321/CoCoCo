package com.kos.CoCoCo.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkManagerMultikey implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long workId;
	private String userId;
}
