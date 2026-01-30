package com.example.demo.dao;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.MenuCategories;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategories, Integer>{

	
}
