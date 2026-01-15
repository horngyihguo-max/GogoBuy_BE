package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Menu;
import com.example.demo.entity.Stores;

@Repository
public interface StoresSearchDao extends JpaRepository<Stores, Integer>{

//	拿取全店家
	@Query(value = "SELECT * FROM stores ", nativeQuery = true)
public List<Stores> getAllStores();
	
//	名字模糊搜尋
	@Query(value = "SELECT * FROM stores " +
            "WHERE name LIKE CONCAT('%', ?1, '%') " +
            "AND is_deleted = false " +
            "ORDER BY id DESC", nativeQuery = true)
public List<Stores> findStoresByNameLike(String name);
	
	@Query(value = "SELECT * FROM stores WHERE id = ?1 AND is_deleted = false", nativeQuery = true)
    public Stores getStoreById(int id);
	
	
	// 取得營業時間
    @Query(value = "SELECT week, open_time as openTime, close_time as closeTime FROM store_operating_hours WHERE stores_id = ?1", nativeQuery = true)
    public List<Map<String, Object>> getOperatingHoursByStoreId(int storeId);

    // 取得菜單品項
    @Query(value = "SELECT id, category_id as categoryId, name, description, base_price as basePrice, image, unusual FROM menu WHERE stores_id = ?1", nativeQuery = true)
    public List<Map<String, Object>> getMenuByStoreId(int storeId);

    // 取得品項類別 (包含價格級距 JSON)
    @Query(value = "SELECT name, price_level as priceLevel FROM menu_categories WHERE stores_id = ?1", nativeQuery = true)
    public List<Map<String, Object>> getCategoriesByStoreId(int storeId);

    // 取得選項群組
    @Query(value = "SELECT id, name, is_required as required, max_selection as maxSelection FROM product_option_groups WHERE stores_id = ?1", nativeQuery = true)
    public List<Map<String, Object>> getOptionGroupsByStoreId(int storeId);

    // 取得選項群組內的細項 (根據群組 ID)
    @Query(value = "SELECT name, extra_price as extraPrice FROM product_option_items WHERE group_id = ?1", nativeQuery = true)
    public List<Map<String, Object>> getOptionItemsByGroupId(int groupId);
    
    
    // (給EVENT)   依值搜尋(多個)品項
    @Query(value = "SELECT * FROM menu WHERE id IN (?1)", nativeQuery = true)
    List<Menu> getMenuByMenuId(List<Integer> menuId);
       
}
