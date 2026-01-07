package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, String> {

//	新增用戶
	@Transactional
	@Modifying
	@Query(value = "insert into user(id, email, password, nickname, phone) "//
			+ " values (?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
	public int addUser(String id, String email, String password, String nickname, String phone);

//	透過 Email 查詢用戶
	@Query(value = "Select * from user where email = ?", nativeQuery = true)
	public User getUser(String email);

//	透過 ID 查詢用戶
	@Query(value = "Select * from user where id = ?", nativeQuery = true)
	public User getUserById(String id);

	
}
