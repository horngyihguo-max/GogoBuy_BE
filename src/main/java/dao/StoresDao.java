package dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import entity.Stores;
import vo.fee_description;

 
@Repository
public interface StoresDao extends JpaRepository<Stores, Integer>{
	
	
	@Modifying
	@Transactional
	@Query(value = "insert into stores(name,phone,address,category,type,//"
			+ "memo,image,fee_description,is_public) "
			+ " values (?1,?2,?3,?4,?5,?6,?7,?8,?9)", nativeQuery = true)
	public int addStore(String name, int  phone, String address, //
			String category, String type, String memo, String image, List <fee_description> fee_description,//
			boolean is_public);
}
