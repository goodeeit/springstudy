package com.gdu.myhome.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BlogDto {
  private int blogNo;
  private String title;
  private String contents;
  private UserDto userDto;  // private int userNo;
  private int hit;
  private String ip;
  private String createdAt;
  private String modifiedAt;
}
