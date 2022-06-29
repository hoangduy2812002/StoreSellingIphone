package com.ps16445.repository;

import java.util.List;
import java.util.Optional;

import com.ps16445.domain.Product;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	List<Product> findByNameContaining(String name);
	Page<Product> findByNameContaining(String name, Pageable pageable);
	
	@Query("select p FROM Product p WHERE p.category.categoryId=?1")
	Page<Product> findByCategoryIdContaining(Long id, Pageable pageable);
	
	@Query("select p FROM Product p WHERE p.unitPrice between?1 AND ?2")
	Page<Product> findByPriceContaining(double min,double max, Pageable pageable);
	
//	@Query("select p FROM Product p WHERE p.productId=?1")
//	List<Product> findByIdCartList(Long productId);
}
