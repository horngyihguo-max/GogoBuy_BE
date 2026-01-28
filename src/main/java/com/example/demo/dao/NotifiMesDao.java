package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.NotifiMes;


@Repository
public interface NotifiMesDao extends JpaRepository<NotifiMes, Integer>{

////	新增訊息
//	@Modifying
//	@Transactional
//	@Query(value = "insert into notification_messages(category, title, content, target_url, expired_at," //
//			+ "user_id, event_id) " //
//			+ " values (?1,?2,?3,?4,?5,?6,?7)", nativeQuery = true)
//	public int createNotifiMes(String category, String title, String content, //
//			String target_url, String expired_at, String user_id, int event_id);
//	
//	@Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
//	public int getLastInsertId();
//	
	
//	訊息ID搜尋
	@Query(value = "select * from notification_messages where id in ?1 ",nativeQuery = true)
	public List<NotifiMes>  searchById(List<Integer> ids);
	
	
}
