package com.ps16445.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.ps16445.domain.Account;
import com.ps16445.domain.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
	List<Account> findByUsernameContaining(String username);
	Page<Account> findByUsernameContaining(String username, Pageable pageable);
	
	
	@Query("select a FROM Account a WHERE a.username=?1")
	Optional<Account> findByUsername(String username);
	
	
	@Query("select a FROM Account a WHERE a.email=?1")
	Optional<Account> findByEmail(String email);
	
	@Modifying
	@Transactional
	@Query("update Account a set a.password=?1 WHERE a.username=?2")
	void forGotPassword(String password, String username);

}
