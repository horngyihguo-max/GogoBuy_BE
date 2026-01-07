package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Stores;
import com.example.demo.vo.FeeDescriptionVo;

import jakarta.transaction.Transactional;

@Repository
public interface StoresDao extends JpaRepository<Stores, Integer> {

//	新增店家
	@Modifying
	@Transactional
	@Query(value = "insert into stores(name, phone, address, category, type," //
			+ "memo, image, fee_description, is_public) " //
			+ " values (?1,?2,?3,?4,?5,?6,?7,?8,?9)", nativeQuery = true)
	public int addStore(String name, String phone, String address, //
			String category, String type, String memo, //
			String image, List<FeeDescriptionVo> fee_description, boolean is_public);
}
