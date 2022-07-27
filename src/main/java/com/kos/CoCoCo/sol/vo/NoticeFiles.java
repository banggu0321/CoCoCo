package com.kos.CoCoCo.sol.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeFiles {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false)
	private String originFname;
	
	@Column(nullable = false)
	private String filename;
	
	@Column(nullable = false)
	private String filePath;
	
	@Builder
	public NoticeFiles(Long id, String originFname, String filename, String filePath) {
		this.id=id;
		this.originFname=originFname;
		this.filename=filename;
		this.filePath=filePath;
	}
	
}
