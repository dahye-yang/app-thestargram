package org.edupoll.app.repository;

import java.util.List;
import java.util.Optional;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Post;
import org.edupoll.app.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>{
	
	public Long countByPost(Post post);
	
	public Optional<PostLike> findByAccount(Account account);
	
	public List<PostLike> findAllByAccountOrderByIdDesc(Account account);
} 
