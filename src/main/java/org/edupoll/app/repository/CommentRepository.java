package org.edupoll.app.repository;

import java.util.List;

import org.edupoll.app.entity.Comment;
import org.edupoll.app.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	public List<Comment> findByPostOrderByIdDesc(Post post);
	
	public List<Comment> findAllByOrderByIdDesc();
}
