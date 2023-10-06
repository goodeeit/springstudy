package com.gdu.app08.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.gdu.app08.dao.MemberDao;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor  // 생성자 주입(@Autowired)을 통해서 private final MemberDao memberDao;에 Bean을 주입한다. 
@Service
public class MemberServiceImpl implements MemberService {

  private final MemberDao memberDao;
  
  // @RequiredArgsConstructor와 동일한 코드는 아래와 같다.
  /*
  @Autowired
  public MemberServiceImpl(MemberDao memberDao) {
    super();
    this.memberDao = memberDao;
  }
  */
  
  @Override
  public Map<String, Object> getBmiInfo(int memberNo) {
    // TODO Auto-generated method stub
    return null;
  }

}
