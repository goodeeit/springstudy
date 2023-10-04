package com.gdu.app04.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.app04.vo.ArticleVo;

@Controller
public class MvcController {

  // DispatcherServlet(servlet-context.xml)에서 ViewResolver를 제거했으므로 JSP의 전체 경로를 모두 작성해야 한다.
  
  @RequestMapping(value="/", method=RequestMethod.GET)
  public String main() {
    return "/WEB-INF/main.jsp";
  }
  
  @RequestMapping(value="/write.do", method=RequestMethod.GET)
  public String write() {
    return "/WEB-INF/article/write.jsp";
  }
  
  // @RequestMapping(value="/register.do", method=RequestMethod.POST)
  public String register(HttpServletRequest request) {
    int articleNo = Integer.parseInt(request.getParameter("articleNo"));
    String title = request.getParameter("title");
    String content = request.getParameter("content");
    System.out.println(articleNo + "," + title + "," + content);
    return "/WEB-INF/article/write.jsp";
  }
  
  // @RequestMapping(value="/register.do", method=RequestMethod.POST)
  public String register2(@RequestParam(value="articleNo") int articleNo
                        , @RequestParam(value="title") String title
                        , @RequestParam(value="content") String content) {
    System.out.println(articleNo + "," + title + "," + content);
    return "/WEB-INF/article/write.jsp";
  }
  
  @RequestMapping(value="/register.do", method=RequestMethod.POST)
  public String register3(ArticleVo vo) {
    System.out.println(vo);
    return "/WEB-INF/article/write.jsp";
  }
  
  
  
  
  
  
  
}
