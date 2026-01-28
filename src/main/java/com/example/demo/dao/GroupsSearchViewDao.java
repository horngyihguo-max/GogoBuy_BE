package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.GroupsSearchView;

public interface GroupsSearchViewDao extends JpaRepository<GroupsSearchView, Integer>{
	
	
	// nickname 查詢 eventId
		@Query(value = "select * from groups_search_view where host_nickname like %?%", nativeQuery = true)
		public List<GroupsSearchView> getGroupbuyEventByNickname (String hostNickname);

	// eventId 查詢 hostNickname
		@Query(value = "select host_nickname from groups_search_view where event_id =?1 ", nativeQuery = true)
		public List<GroupsSearchView> getNicknameByEventId (int eventId);
		
}
