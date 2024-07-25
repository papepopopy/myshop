package com.spring.myproject.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.myproject.constant.ItemSellStatus;
import com.spring.myproject.entity.Item;
import com.spring.myproject.entity.QItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
@TestPropertySource(locations = {"classpath:application-test.properties"})
class ItemRepositoryTest {
  @Autowired
  private ItemRepository itemRepository;

  @PersistenceContext
  EntityManager em;

  @Test
  @DisplayName("상품 등록 테스트")
  public void insertItemList(){
    IntStream.rangeClosed(1,10).forEach(i->{

      Item item = Item.builder()
          .itemNm("테스트 상품"+i)
          .price(10000+i)
          .itemDetail("테스트 상품 상세 설명"+i)
          .itemSellStatus(ItemSellStatus.SELL)
          .stockNumber(100)
          .regTime(LocalDateTime.now())
          .updateTime(LocalDateTime.now())
          .build();

      Item savedItem = itemRepository.save(item);
    });
  }
  @Test
  @DisplayName("상품명 조회 테스트")
  public void findByItemNmTest(){
    // 상품 등록 ,
    this.insertItemList();//h2 db적용시
    // 상품 조회
    List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
    // stream().forEach()
    itemList.stream().forEach(item -> {
      log.info("=> item: "+item);
    });

  }

  @Test
  @DisplayName("상품명, 상품상세설명 or 테스트")
  public void findByItemNmOrItemDetailTest(){
    // 상품 등록 ,
    this.insertItemList();//h2 db적용시

    List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
    // 확장for
    for(Item item : itemList){
      log.info(item);
    }
  }

  @Test
  @DisplayName("가격 LessThan 테스트")
  public void findByPriceLessThenTest(){
    // 상품 등록 ,
    this.insertItemList();//h2 db적용시

    List<Item> itemList = itemRepository.findByPriceLessThan(10005);
    itemList.stream().forEach( item -> log.info("=> Less Than: "+item));
  }
  @Test
  @DisplayName("가격 내림차순 and LessThan 테스트")
  public void findPriceLessThanOrderByPriceDescTest(){
    // 상품 등록 ,
    this.insertItemList();//h2 db적용시
    List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
    itemList.stream().forEach( item -> log.info("=> Order Desc: "+item));
  }


  // ----------------------------------------- //
  // Spring DATA JPA @query어노테이션 테스트
  // ----------------------------------------- /
  @Test
  @DisplayName(" JPA @query어노테이션 테스트")
  public void findByItemDetailTest(){
    // 상품 등록 ,
    this.insertItemList();//h2 db적용시

    //List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명1");
    List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명1");
    itemList.stream().forEach( item -> {
      log.info("=> item list: "+item);
    });
  }


  // ----------------------------------------- //
  // Spring Q도메인 클래스 적용
  // ----------------------------------------- /
  @Test
  @DisplayName("QueryDsl을 적용하여 조회 기능 테스트")
  public void queryDslTest(){
    /*
      // 5. jpa queryDsl error => "Attempt to recreate a file" 도메인 중복 발생
      // IntelliJ 셋팅에서  Gradle: IntelliJ -> Gradle로 변환   or
      // IntelliJ 셋탱에서  Gradle: Gradle -> IntelliJ로
    */

    // 상품 등록 ,
    this.insertItemList();//h2 db적용시

    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
    QItem qItem = QItem.item;

    // 동적 query문 작성
    JPAQuery<Item> query = queryFactory
        .selectFrom(qItem)
        .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
        .where(qItem.itemDetail.like("%"+"상품 상세 설명"+"%"))
        .orderBy(qItem.price.desc());

    // 1. List<T> fetch() => query문 실행(조회한 결과를 List로 반환)
    List<Item> itemList = query.fetch();
    log.info("=> item list: .fetch() 메서드 결과");
    itemList.forEach(item -> log.info("=> Qitem: "+item.getId()+","+item.getItemDetail()));

    Item itemOne = query.fetchFirst();
    log.info("=> item list: .fetchFirst() 메서드 결과");
    log.info("=> Qitem: "+itemOne.getId()+","+itemOne.getItemDetail());

    Long total = query.fetchCount();
    log.info("=> item list: .fetchCount() 메서드 결과");
    log.info("=> total: "+total);

    // 단일 결과 처리
    JPAQuery<Item> query2 = queryFactory.selectFrom(qItem).where(qItem.id.eq(5L));
    Item resultOne  = query2.fetchOne();
    log.info("=> .fetchOne() 조회결과: ");
    log.info("=> "+resultOne.getId()+","+resultOne.getItemNm());

  }
  // ----------------------------------------- //
  // Spring Q도메인 클래스 적용: QuerydslPredicateExecutor 테스트
  // ----------------------------------------- /
  @Test
  @DisplayName("QueryDsl:조회기능 테스트2")
  public void queryDslTest2(){
    // 상품 등록 ,
    this.insertItemList();//h2 db적용시

    BooleanBuilder booleanBuilder = new BooleanBuilder();
    QItem item = QItem.item;

    // 조건 검색할 대상을 초기값을 설정변수
    String itemDetail = "테스트 상품 상세 설명";
    int price = 10003;
    String search_itemSellStattus = "SELL";

    // query에 필요한 조건 설정
    booleanBuilder.and(item.itemDetail.like("%"+itemDetail+"%"));
    booleanBuilder.and(item.price.gt(price));

    // 판매 상태에 대한 검색키워드가 있으면 판매상태 조건문 추가
    if(StringUtils.equals(search_itemSellStattus, ItemSellStatus.SELL)){
      booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL)); // where itemSellStatus = 'SELL'
    }

    // 페이징
    Pageable pageable = PageRequest.of(0,5);//(페이지번호, 페이지당 데이터개수)
    //List<Item> resultItemList =

  }

}

/*
JPAQuery 데이터 변환 메서드
List<T> fetch() : 조회한 결과를 List로 반환
T fetchOne(): 조회 대상이 1건인 경우 제네릭으로 지정한 타입 반환
T fetchFirst(): 조회 대상 중 1건만 반환
Long fetchCount(): 조회 대상 개수 반환
=>  fetchCount()메서드 대신 : .fetch().size()형식으로 변경
QueryResult<T> fetchResults()
  : 조회한 List와 전체 개수를 포함한 QueryResults반환


QueryDslPredidicateExecuter적용하여 상품 조회 기능 구현
(Priedicate: 조건이 맞는지 판별)

Repositiory에서는 Predicate를 파라미터로 전달하기위해
 => QueryDslPredicateExecutor인터페이스를 상속

 */