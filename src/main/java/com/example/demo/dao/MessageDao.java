package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Message;
import com.example.demo.entity.Wishes;

public interface MessageDao extends JpaRepository<Message, Integer>{
//	抓單人所有通知
	@Query(value = "select * from message where user_id = ?", nativeQuery = true)
	public List<Message> allMes(String userId);
}
