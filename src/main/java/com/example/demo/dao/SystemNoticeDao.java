package com.example.demo.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.SystemNotice;
import com.example.demo.entity.UserInfo;

@Repository
public interface SystemNoticeDao extends JpaRepository<SystemNotice, Integer> {

	// 取得所有公告
	@Query(value = "Select * from system_notice", nativeQuery = true)
	public List<SystemNotice> findAllNotice();

	// 新增公告
	@Transactional
	@Modifying
	@Query(value = "insert into system_notice(content, created_at, expired_at) "//
			+ " values (?1, ?2, ?3)", nativeQuery = true)
	public int addNotice(String content, LocalDateTime cteateAt, LocalDateTime expireAt);

}
