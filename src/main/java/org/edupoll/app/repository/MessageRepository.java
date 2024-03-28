package org.edupoll.app.repository;

import java.util.List;

import org.edupoll.app.entity.Chatroom;
import org.edupoll.app.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long>{
	
	public List<Message> findByChatroomOrderBySentAtAsc(Chatroom chatroom);
	
	public List<Message> findByChatroomIdOrderByIdDesc(Long chatroomId);
	public List<Message> findByChatroomIdOrderByIdAsc(Long chatroomId);
	
	

}
