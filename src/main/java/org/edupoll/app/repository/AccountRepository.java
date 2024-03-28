package org.edupoll.app.repository;

import java.util.Optional;

import org.edupoll.app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
	
	public Optional<Account> findByUsername(String username);
	
	public Optional<Account> findByEmail(String email);
	

}
