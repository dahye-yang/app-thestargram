package org.edupoll.app.repository;

import java.util.List;
import java.util.Optional;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long>{
	
	public List<Chatroom> findByCreater(Account account);
	
	// 로그인 계정의 대화방 전체
	public List<Chatroom> findByCreaterOrInvitee(Account creater, Account invitee);
	
	// 채팅방 중복체크
	public Chatroom findByCreaterAndInvitee(Account creater, Account invitee);
	
	public Optional<Chatroom> findById(Long roomId);

}
