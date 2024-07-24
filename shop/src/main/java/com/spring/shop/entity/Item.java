package com.spring.shop.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.spring.shop.constant.ItemSellStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

//엔티티 : 데이터베이스의 테이블에 대응하는 클래스, JPA에서 관리
@Entity
@Table(name = "item") //table 명

@Data
@AllArgsConstructor
@Builder
public class Item {
	@Id
	@Column(name = "item_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; //상품코드
	
	@Column(nullable = false, length = 50)
	private String itemNm; //상품명
	
	@Column(name = "price", nullable=false)
	private int price; //상품가격
	
	//db컬럼 정보 직접 기술 
	@Column(columnDefinition = "int not null defult 0")
	//@Column(nullable = false)
	private int stockNumber; //재고수량
	
	@Lob //긴 데이터의 경우 사용한다.
	@Column(nullable = false)
	private String itemDetail; //상품상세설명
	
	@Enumerated(EnumType.STRING)
	private ItemSellStatus itemSellStatus; //상품 판매상태
	
	private LocalDate regTime; //등록시간
	private LocalDateTime updateTime; //수정시간
}

/*
 * GenerationType.IDETITY : 기본 키 생성을 db에 위임 (mysql, mariadb인 경우 auto_increment)
 * GenerationType.AUTO : JPA 구현체가 자동으로 생성 전략 결정
 * GenerationType.SEQUENCE : DB 시퀴스 오브젝트를 이용한 기본키 생성 @SequenceGenerator를 사용하여 시퀸스 이용
 * GenerationType.TABLE : 
 * 
 * */
