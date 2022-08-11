package com.kos.CoCoCo.ja0.VO;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class KakaoUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
    private String name;
    private String email;
    private String img;
    
}
