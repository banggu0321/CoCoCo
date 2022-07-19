package com.kos.CoCoCo.vo;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "boardCategory")
public class BoardCategoryVO {

	@EmbeddedId
	BoardCategoryMultikey boardCategoryId;
	
	private String categoryName;

}
