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

public class BoardCategoryMultikey implements Serializable{ //Composite-id class must implement Serializable
	private static final long serialVersionUID = 1L;

	private Long categoryId;
	private Long teamId;
}
