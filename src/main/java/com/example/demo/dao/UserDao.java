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
	public User getUserByEmail(String email);

//	透過 ID 查詢用戶
	@Query(value = "Select * from user where id = ?", nativeQuery = true)
	public User getUserById(String id);

//	更改使用者個人資訊(暱稱、大頭貼、統編)
	@Modifying
	@Transactional
	@Query(value = "update user SET nickname = ?1, avatar_url = ?2, carrier = ?3 WHERE id = ?4", nativeQuery = true)
	public int updateProfile(String nickname, String avatarUrl, //
			String carrier, String id);

//	更改密碼
	@Transactional
	@Modifying
	@Query(value = "update user SET password = ?2 where id = ?1", nativeQuery = true)
	public int userPassword(String id, String password);

//	串接電話
	@Transactional
	@Modifying
	@Query(value = "update user SET phone = ?2 where id = ?1", nativeQuery = true)
	public int userPhone(String id, String phone);

//	透過 ID 查詢用戶
	@Query(value = "SELECT EXISTS(SELECT 1 FROM user WHERE email = ?)", nativeQuery = true)
	public int existsByEmail(String email);
}
