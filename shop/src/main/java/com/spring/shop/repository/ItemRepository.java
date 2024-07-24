package com.spring.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.shop.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> { //테이블명, 타입
	
	List<Item> findByItemNm(String itemNm);
	
}

/*
 * JpaRepository 지원하는 메서드
 * <S extends T> save(S entity) : 저장 및 수령
 * void delete(t entity) : 삭제
 * count() : 총 개수 반환
 * Iterable<T> findAll() : 모든 엔티티 조회
 * 
 * 
 */