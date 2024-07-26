package com.spring.myproject.entity;

import jakarta.persistence.*;
import lombok.*;

// Entity 정의 : 테이블에 적용될 구조설계 정의하여 테이블과 entity 1:1 맵핑
@Entity@Table(name="board")
@Getter@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Board extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bno;

  @Column(length = 500, nullable = false)
  private String title;
  @Column(length = 2000, nullable = false)
  private String content;
  @Column(length = 50, nullable = false)
  private String writer;

  // 데이터 수정하는 메서드
  public void change(String title, String content){
    this.title = title;
    this.content = content;
  }
}
