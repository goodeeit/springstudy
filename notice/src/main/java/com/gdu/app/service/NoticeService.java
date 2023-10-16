package com.gdu.app.service;

import java.util.List;

import com.gdu.app.dto.NoticeDto;

public interface NoticeService {
  int addNotice(NoticeDto noticeDto);
  List<NoticeDto> getNoticeList();
}
