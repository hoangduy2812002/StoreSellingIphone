package com.ps16445.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.ps16445.domain.Order;
import com.ps16445.repository.OrderRepository;
import com.ps16445.service.OrderService;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService{
	
	OrderRepository orderRepository;

	public OrderServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Override
	public <S extends Order> S save(S entity) {
		return orderRepository.save(entity);
	}

	@Override
	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	@Override
	public Page<Order> findAll(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}

	@Override
	public void deleteById(Long id) {
		orderRepository.deleteById(id);
	}

	public Optional<Order> findById(Long id) {
		return orderRepository.findById(id);
	}

	@Override
	public long count() {
		return orderRepository.count();
	}


	
}
