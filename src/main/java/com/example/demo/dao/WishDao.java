package com.example.demo.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Wishes;
import com.example.demo.request.WishReq;

import jakarta.transaction.Transactional;

@Repository
public interface WishDao extends JpaRepository<Wishes, Integer>{
//	新增願望
	@Modifying
	@Transactional
	@Query(value = "insert into wishes (user_id, title, is_anonymous, followers, is_deleted, type, location)" //
			+ " values (?1, ?2, ?3, ?4, ?5, ?6, ?7)", nativeQuery = true)
	public void addWish(String userId, String title, boolean anonymous, String followers, boolean deleted, //
			String type, String location);
	
//	查詢剩餘次數
	@Query(value = "select times_remaining from user where id = ?", nativeQuery = true)
	public int getTimes(String id);
//	更新次數
	@Modifying
	@Transactional
	@Query(value = "update user set times_remaining = ?2 where id = ?1", nativeQuery = true)
	public int setTimes(String id, int times);

//	每月許願次數重新計算
	@Modifying
	@Transactional
	@Query(value = "update user set times_remaining = ?3 where exp >= ?1 and exp <= ?2", nativeQuery = true)
	public void wishTimesReset(int minExp, int maxExp, int times);
	
//	查詢該願望的follower
	@Query(value = "select user_id, followers from wishes where id = ?", nativeQuery = true)
	public List<Object[]> getfollowers(int id);
//	許願followers更新
	@Modifying
	@Transactional
	@Query(value = "update wishes set followers = ?2 where id = ?1", nativeQuery = true)
	public void setfollowers(int id, String newFollowersStr);
	
//	刪除願望
	@Modifying
	@Transactional
	@Query(value = "update wishes set is_deleted = true where id = ?1 and user_id = ?2", nativeQuery = true)
	public int delWish(int id, String userId);
}
