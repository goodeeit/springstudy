package com.gdu.app14.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.app14.dto.MemberDto;

@Mapper
public interface MemberMapper {

  // 삽입
  public int insertMember(MemberDto memberDto);
  
}
