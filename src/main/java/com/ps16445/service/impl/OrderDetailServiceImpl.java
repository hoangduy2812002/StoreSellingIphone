package com.ps16445.service.impl;


import java.util.List;
import java.util.Optional;

import com.ps16445.domain.OrderDetail;

import com.ps16445.repository.OrderDetailRepository;
import com.ps16445.service.OrderDetailService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope
public class OrderDetailServiceImpl implements OrderDetailService {

	OrderDetailRepository orderDetailRepository;

	public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository) {
//		super();
		this.orderDetailRepository = orderDetailRepository;
	}

	@Override
	public <S extends OrderDetail> S save(S entity) {
		return orderDetailRepository.save(entity);
	}


	@Override
	public Page<OrderDetail> findAll(Pageable pageable) {
		return orderDetailRepository.findAll(pageable);
	}

	@Override
	public Optional<OrderDetail> findById(Long id) {
		return orderDetailRepository.findById(id);
	}

	@Override
	public List<OrderDetail> findAllById(Iterable<Long> ids) {
		return orderDetailRepository.findAllById(ids);
	}

	@Override
	public Page<OrderDetail> findByorderIdContaining(Long orderDetailId, Pageable pageable) {
		return orderDetailRepository.findByorderIdContaining(orderDetailId, pageable);
	}

	@Override
	public void deleteById(Long id) {
		orderDetailRepository.deleteById(id);
	}


}
