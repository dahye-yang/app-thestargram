package org.edupoll.app.repository;

import java.util.List;
import java.util.Optional;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Post;
import org.edupoll.app.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long>{
	
	public List<Post> findByAccountOrderByIdDesc(Account account);
	
	public Long countByAccount(Account account);
	
	//public List<Post> findAllByOrderByIdDesc();
	
	public Optional<Post> findById(Long id);
	
	@Query(value = "select * from post where account_id IN (select follow from follow where follower = :follower)", nativeQuery = true)
	List<Post> findAllByOrderByIdDesc(@Param("follower") Long accountId);
}
