package org.edupoll.app.repository;

import java.util.List;
import java.util.Optional;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	
	public Optional<Profile> findByAccount(Account acount);
	// index 화면에서의 친구추천 5명
	@Query(value = "SELECT * FROM profile where account_id NOT IN (select follow from follow where follower = :accountId) and account_id != :accountId order by rand() limit 5", nativeQuery = true)
	List<Profile> findAll(Long accountId);
//	@Query(value = "SELECT * FROM profile where account_id not in(:account_id) order by rand() limit 5", nativeQuery = true)
//		List<Profile> findAll(@Param("account_id") Long accountId);
	
	@Query(value = "SELECT * FROM profile where account_id not in(:account_id)", nativeQuery = true)
		List<Profile> findAll1(@Param("account_id") Long accountId);
	
	// 친구추천 본인,팔로우된 친구들 제외한 프로필들
	@Query(value = "SELECT * FROM profile where account_id NOT IN (select follow from follow where follower = :accountId) and account_id != :accountId", nativeQuery = true)
	List<Profile> findAll2(Long accountId);
	
	// 팔로워 찾기
	@Query(value = "SELECT * FROM profile a join (select follow from follow where follower = :accountId) b on a.account_id = b.follow", nativeQuery = true)
	List<Profile> findAll3(Long accountId);
	
	// 팔로우 찾기
	@Query(value = "SELECT * FROM profile a join (select follower from follow where follow = :accountId) b on a.account_id = b.follower", nativeQuery = true)
	List<Profile> findAll4(Long accountId);
}
