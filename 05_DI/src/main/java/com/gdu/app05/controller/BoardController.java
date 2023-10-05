package com.gdu.app05.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gdu.app05.service.BoardService;

@Controller  // 컨트롤러 전용 @Component
public class BoardController {
  
  @Autowired
  private BoardService boardService;
  
//  @Autowired  
//  public BoardController(BoardService boardService) {
//    super();
//    this.boardService = boardService;
//  }
//
//  @Autowired
//  public void setBoardService(BoardService boardService) {
//    this.boardService = boardService;
//  }

  @RequestMapping(value="/board/list.do", method=RequestMethod.GET)
  public String list(Model model) {
    model.addAttribute("boardList", boardService.getBoardList());
    return "board/list";
  }
  
}
