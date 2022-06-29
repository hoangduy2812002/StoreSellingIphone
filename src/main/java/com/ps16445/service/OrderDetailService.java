package com.ps16445.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.ps16445.domain.OrderDetail;
import com.ps16445.model.OrderDetailDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderDetailService {

	<S extends OrderDetail> S save(S entity);

//	Page<OrderDetail> findByNameContaining(String name, Pageable pageable);

	Page<OrderDetail> findAll(Pageable pageable);

	List<OrderDetail> findAllById(Iterable<Long> ids);

	Optional<OrderDetail> findById(Long id);

	Page<OrderDetail> findByorderIdContaining(Long orderDetailId, Pageable pageable);

	void deleteById(Long id);

}
