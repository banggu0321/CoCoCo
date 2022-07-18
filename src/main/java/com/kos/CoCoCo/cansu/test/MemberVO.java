package com.kos.CoCoCo.cansu.test;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data //@Getter, @Setter, @ToString, @EqualsandHashCode
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "mid")
@Entity
@Table(name = "tbl_members")
public class MemberVO {

	@Id
	String mid;
	String mpassword;
	String mname;
}