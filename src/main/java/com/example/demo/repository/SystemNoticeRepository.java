package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.SystemNotice;

@Repository
public interface SystemNoticeRepository extends JpaRepository<SystemNotice, Integer> {
    
    // 依建立時間倒序取得所有公告
    List<SystemNotice> findAllByOrderByCreatedAtDesc();
}
