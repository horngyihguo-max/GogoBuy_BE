package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.ProductOptionGroups;

@Repository
public interface ProductOptionGroupsDao extends JpaRepository<ProductOptionGroups, Integer> {
    // 繼承 JpaRepository 之後，save() 方法就會自動出現且支援自動獲取 ID
}
