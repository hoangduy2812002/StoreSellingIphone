package com.ps16445.repository;

import com.ps16445.domain.OrderDetail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>{
	
	@Query("select o FROM OrderDetail o WHERE o.order.orderId=?1")
	Page<OrderDetail> findByorderIdContaining(Long orderDetailId, Pageable pageable);
}
