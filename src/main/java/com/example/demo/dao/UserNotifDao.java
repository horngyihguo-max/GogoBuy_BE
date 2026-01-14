package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.UserNotif;

import jakarta.transaction.Transactional;

public interface UserNotifDao extends JpaRepository<UserNotif,Integer>{

	
//	新增用戶訊息
	@Modifying
	@Transactional
	@Query(value = "insert into user_notification(user_id, notif_id) " //
			+ " values (?1,?2)", nativeQuery = true)
	public int createUserNotifi(String userId, int  notifId);
	
	// 根據訊息 ID 刪除所有關聯 (Update 時使用)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_notification WHERE notif_id = ?1", nativeQuery = true)
    void deleteByNotifId(int notifId);

    // 根據訊息 ID 查詢所有關聯 (回傳結果時使用)
    @Query(value = "SELECT * FROM user_notification WHERE notif_id = ?1", nativeQuery = true)
    List<UserNotif> findByNotifId(int notifId);
    /*Optional>>可能有可能沒有(什麼虛無飄渺的怪東西?)
     * 
     * 常用方法：

findById(id)：這是 JPA 內建的方法，它不直接回傳 Entity，而是回傳 Optional<Entity>。

.orElse(null)：如果裡面有值就拿出來，沒值就給 null。

.orElseThrow(...)：如果沒值就直接拋出例外（這是最推薦的作法）。

.isPresent()：檢查裡面是否有值。*/
    Optional<UserNotif> findByUserIdAndNotifId(String userId, int notifId);
    
    @Modifying
	@Transactional
    @Query(value = "UPDATE user_notification SET is_deleted = true WHERE user_id = ?1 AND notif_id = ?2", nativeQuery = true)
    void softDelete(String userId, int notifId);
    
}
