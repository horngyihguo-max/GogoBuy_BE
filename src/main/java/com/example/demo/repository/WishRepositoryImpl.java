package com.example.demo.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.WishDao;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Repository
public class WishRepositoryImpl implements WishRepository {
	@Autowired
	private EntityManager entityManager;
	
	@Transactional
	public void addRecipientsBatch(int messageId, List<String> wishersList) {
		for(String user:wishersList) {
			entityManager.createNativeQuery(
					"insert into user_notification (user_id, notif_id) values (:userId, :notifId)")
			.setParameter("userId", user)
			.setParameter("notifId", messageId)
			.executeUpdate();
		}
	}
}
