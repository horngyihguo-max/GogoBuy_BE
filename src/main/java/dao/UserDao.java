package dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import entity.User;

@Repository
public interface UserDao extends JpaRepository<User, String>{
	
	@Query(value ="select * from user where email = ?", nativeQuery = true)
	public User getUser(String email);
	
}
