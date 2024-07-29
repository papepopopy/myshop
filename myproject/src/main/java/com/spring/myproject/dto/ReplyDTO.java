package com.spring.myproject.dto;

import io.swagger.v3.oas.annotations.callbacks.Callback;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
  private Long rno;

  @NotNull
  private Long bno;// 댓글의 부모
  @NotEmpty
  private String replyText;
  @NotEmpty
  private String replyer;

  private LocalDateTime regDate, modDate;
}
