package com.gdu.bbs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gdu.bbs.dto.BbsDto;
import com.gdu.bbs.service.BbsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BbsController {

  private final BbsService bbsService;
  
  @GetMapping("/list.do")
  public String list(HttpServletRequest request, Model model) {
    bbsService.loadBbsList(request, model);
    return "bbs/list";
  }
  
  @GetMapping("/detail.do")
  public String detail(@RequestParam(value="bbsNo", required=false, defaultValue="0") int bbsNo
                     , Model model) {
    BbsDto bbs = bbsService.getBbs(bbsNo);
    model.addAttribute("bbs", bbs);
    return "bbs/detail";
  }
  
  
  
  
}
