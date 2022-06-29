package com.ps16445.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.ps16445.domain.Order;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

public interface OrderService {

	void deleteById(Long id);

	Page<Order> findAll(Pageable pageable);

	List<Order> findAll();

	<S extends Order> S save(S entity);

	long count();
	
}
