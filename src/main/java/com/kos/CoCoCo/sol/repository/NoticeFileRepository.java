package com.kos.CoCoCo.sol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.kos.CoCoCo.sol.vo.NoticeFile;

public interface NoticeFileRepository extends CrudRepository<NoticeFile, Long> {
	
	@Query(value="select f.file_id, f.origin_fname, f.FILENAME from notice_file f"
			+ " where f.notice_notice_id = ?1", nativeQuery=true)
	public List<NoticeFile> findByNoticeId(Long noticeId);

	
	
	

}
