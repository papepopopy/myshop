package com.spring.shop.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;

import com.spring.shop.constant.ItemSellStatus;
import com.spring.shop.entity.Item;



@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@WebAppConfiguration
class ItemRepositoryTest {
	static Logger logger = LoggerFactory.getLogger(ItemRepository.class);
	
	@Autowired
	ItemRepository itemRepository;
	
	@Test
	@DisplayName("상품 저장 repository 테스트")
	public void createItemTest() {
		Item item = Item.builder()
				.itemNm("베스트 상품")
				.price(10000)
				.itemDetail("테스트 상품 상세 설명")
				.itemSellStatus(ItemSellStatus.SELL)
				.regTime(LocalDate.now())
				.updateTime(LocalDateTime.now())
				.build();
		
		//엔티티를 연속성 컨테스트을 통해 DB 등록
		Item saveItem = itemRepository.save(item);
		logger.info("저장후 반환값 ", saveItem);
	}
	//상품 등록
	public void createItemList() {
		IntStream.rangeClosed(1, 10).forEach(num -> {
			Item item = Item.builder()
					.itemNm("테스트 상품"+num)
					.price(10000+num)
					.itemDetail("테스트 상품 상세 설명"+num)
					.itemSellStatus(ItemSellStatus.SELL)
					.regTime(LocalDate.now())
					.updateTime(LocalDateTime.now())
					.build();
			//비영속성
			Item saveItem = itemRepository.save(item);
		});
	}
	
	//상품 조회
	@Test
	@DisplayName("상품명 조회 테스트")
	public void findByItemNmTest() {
		this.createItemList();
		
		List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
		for (Item item : itemList) {
			logger.info("item", item);
		}
	}
}