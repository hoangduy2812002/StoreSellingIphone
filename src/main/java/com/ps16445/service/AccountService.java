package com.ps16445.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.ps16445.domain.Account;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

public interface AccountService {

	<S extends Account> List<S> findAll(Example<S> example, Sort sort);

	<S extends Account> List<S> findAll(Example<S> example);

	Account getReferenceById(Long id);

	Account getById(Long id);

	void deleteAll();

	void deleteAll(Iterable<? extends Account> entities);

	Account getOne(Long id);

	void deleteAllInBatch();

	void deleteAllById(Iterable<? extends Long> ids);

	<S extends Account, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction);

	void delete(Account entity);

	void deleteAllByIdInBatch(Iterable<Long> ids);

	void deleteById(Long id);

	<S extends Account> boolean exists(Example<S> example);

	long count();

	void deleteAllInBatch(Iterable<Account> entities);

	<S extends Account> long count(Example<S> example);

	void deleteInBatch(Iterable<Account> entities);

	<S extends Account> Page<S> findAll(Example<S> example, Pageable pageable);

	<S extends Account> List<S> saveAllAndFlush(Iterable<S> entities);

	<S extends Account> S saveAndFlush(S entity);

	boolean existsById(Long id);

	void flush();

	<S extends Account> List<S> saveAll(Iterable<S> entities);

	Optional<Account> findById(Long id);

	List<Account> findAllById(Iterable<Long> ids);

	List<Account> findAll(Sort sort);

	Page<Account> findAll(Pageable pageable);

	List<Account> findAll();

	<S extends Account> Optional<S> findOne(Example<S> example);

	<S extends Account> S save(S entity);

	Page<Account> findByUsernameContaining(String username, Pageable pageable);

	List<Account> findByUsernameContaining(String username);

	Optional<Account> findByUsername(String username);

	Account login(String username, String password);

	Account forgotUsername(String username);

	Account forgotEmail(String email);

	void forGotPassword(String password, String username);

}
