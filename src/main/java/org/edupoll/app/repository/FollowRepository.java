package org.edupoll.app.repository;

import java.util.List;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Follow;
import org.edupoll.app.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FollowRepository extends JpaRepository<Follow, Long> {
	
	// 내가 팔로우한 숫자
	public Long countByFollow(Account follow); 
	
	// 날 팔로우한 숫자
	public Long countByFollower(Account follower); 
	
	// 날 팔로우 한 팔로워 찾기
	public List<Follow> findByFollow(Account account);
	
	// 내가 팔로워 한 팔로우 찾기
	public List<Follow> findByFollower(Account account);
	
	// 팔로워 삭제하기
	@Modifying
	@Query(value = "DELETE FROM follow WHERE follow = ?1 AND follower = ?2", nativeQuery = true)
	void deleteByFollowAndFollower(Long followId,Long accountId);
}
