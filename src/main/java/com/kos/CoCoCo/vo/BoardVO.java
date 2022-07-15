package com.kos.CoCoCo.vo;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.kos.CoCoCo.cansu.MemberVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "boards")
public class BoardVO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long boardId;
	
	private Long categoryId;
	
	private String boardTitle;
	
	private String boardText;
	
	private String userId;
	
	@CreationTimestamp
	private Timestamp boardRegDate;
	
	@UpdateTimestamp
	private Timestamp boardUpdate;
	
	private String boardFile;
}
