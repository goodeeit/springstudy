package com.gdu.myhome.service;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.myhome.dao.BlogMapper;
import com.gdu.myhome.dto.BlogDto;
import com.gdu.myhome.dto.BlogImageDto;
import com.gdu.myhome.dto.CommentDto;
import com.gdu.myhome.dto.UserDto;
import com.gdu.myhome.util.MyFileUtils;
import com.gdu.myhome.util.MyPageUtils;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService {

  private final BlogMapper blogMapper;
  private final MyFileUtils myFileUtils;
  private final MyPageUtils myPageUtils;
  
  @Override
  public Map<String, Object> imageUpload(MultipartHttpServletRequest multipartRequest) {
    
    // 이미지가 저장될 경로
    String imagePath = myFileUtils.getBlogImagePath();
    File dir = new File(imagePath);
    if(!dir.exists()) {
      dir.mkdirs();
    }
    
    // 이미지 파일 (CKEditor는 이미지를 upload라는 이름으로 보냄)
    MultipartFile upload = multipartRequest.getFile("upload");
    
    // 이미지가 저장될 이름
    String originalFilename = upload.getOriginalFilename();
    String filesystemName = myFileUtils.getFilesystemName(originalFilename);
    
    // 이미지 File 객체
    File file = new File(dir, filesystemName);
    
    // 저장
    try {
      upload.transferTo(file);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    // CKEditor로 저장된 이미지의 경로를 JSON 형식으로 반환해야 함
    return Map.of("uploaded", true
                , "url", multipartRequest.getContextPath() + imagePath + "/" + filesystemName);
    
    // url: "http://localhost:8080/myhome/blog/2023/10/27/파일명"
    // sevlet-context.xml에
    // /blog/** 주소 요청을 /blog 디렉터리로 연결하는 <resources> 태그를 추가해야 함
    
  }
  
  @Override
  public int addBlog(HttpServletRequest request) {

    //** 수정된 메소드 **//
    
    // BLOG_T에 추가할 데이터
    String title = request.getParameter("title");
    String contents = request.getParameter("contents");
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    String ip = request.getRemoteAddr();
    
    // BlogDto 생성
    BlogDto blog = BlogDto.builder()
                    .title(title)
                    .contents(contents)
                    .userDto(UserDto.builder()
                              .userNo(userNo)
                              .build())
                    .ip(ip)
                    .build();
    
    // BLOG_T에 추가
    // BlogMapper의 insertBlog() 메소드를 실행하면
    // insertBlog() 메소드로 전달한 blog 객체에 blogNo값이 저장된다.
    int addResult = blogMapper.insertBlog(blog);
    
    // Editor에 추가한 이미지 목록 가져와서 BLOG_IMAGE_T에 저장하기
    for(String editorImage : getEditorImageList(contents)) {
      BlogImageDto blogImage = BlogImageDto.builder()
          .blogNo(blog.getBlogNo())
          .imagePath(myFileUtils.getBlogImagePath())
          .filesystemName(editorImage)
          .build();
      blogMapper.insertBlogImage(blogImage);
    }
    
    return addResult;
    
  }
  
  public List<String> getEditorImageList(String contents) {
    
    //** 신규 메소드 **//
    // Editor에 추가한 이미지 목록 반환하기 (Jsoup 라이브러리 사용)
    
    List<String> editorImageList = new ArrayList<>();
    
    Document document = Jsoup.parse(contents);
    Elements elements =  document.getElementsByTag("img");
    
    if(elements != null) {
      for(Element element : elements) {
        String src = element.attr("src");
        String filesystemName = src.substring(src.lastIndexOf("/") + 1);
        editorImageList.add(filesystemName);
      }
    }
    
    return editorImageList;
    
  }

  
  @Transactional(readOnly=true)
  public void blogImageBatch() {
    
    // 1. 어제 작성된 블로그의 이미지 목록 (DB)
    List<BlogImageDto> blogImageList = blogMapper.getBlogImageInYesterday();
    
    // 2. List<BlogImageDto> -> List<Path> (Path는 경로+파일명으로 구성)
    List<Path> blogImagePathList = blogImageList.stream()
                                                .map(blogImageDto -> new File(blogImageDto.getImagePath(), blogImageDto.getFilesystemName()).toPath())
                                                .collect(Collectors.toList());
    
    // 3. 어제 저장된 블로그 이미지 목록 (디렉토리)
    File dir = new File(myFileUtils.getBlogImagePathInYesterday());
    
    // 4. 삭제할 File 객체들
    File[] targets = dir.listFiles(file -> !blogImagePathList.contains(file.toPath()));

    // 5. 삭제
    if(targets != null && targets.length != 0) {
      for(File target : targets) {
        target.delete();
      }
    }
    
  }
  
  @Transactional(readOnly=true)
  @Override
  public void loadBlogList(HttpServletRequest request, Model model) {
  
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = blogMapper.getBlogCount();
    int display = 10;
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd());
    
    List<BlogDto> blogList = blogMapper.getBlogList(map);
    
    model.addAttribute("blogList", blogList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/blog/list.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    
  }
  
  @Override
  public int increseHit(int blogNo) {
    return blogMapper.updateHit(blogNo);
  }
  
  @Transactional(readOnly=true)
  @Override
  public BlogDto getBlog(int blogNo) {
    return blogMapper.getBlog(blogNo);
  }

  @Override
  public int modifyBlog(HttpServletRequest request) {
    
    //** 수정된 메소드 **//
    
    // 수정할 제목/내용/블로그번호
    String title = request.getParameter("title");
    String contents = request.getParameter("contents");
    int blogNo = Integer.parseInt(request.getParameter("blogNo"));
    
    // 기존 이미지
    List<BlogImageDto> blogImageList = blogMapper.getBlogImageList(blogNo);
        
    // Editor 이미지
    List<String> editorImageList = getEditorImageList(contents);
    
    // 기존 이미지에 있고, Editor에 없는 이미지는 기존 이미지를 삭제해야 함
    List<File> removeList = blogImageList.stream()
                              .filter(blogImage -> !editorImageList.contains(blogImage.getFilesystemName()))
                              .map(blogImage -> new File(blogImage.getImagePath(), blogImage.getFilesystemName()))
                              .collect(Collectors.toList());
    System.out.println(removeList);
    
    // 기존 이미지에 없고, Editor에 있는 이미지는 Editor 이미지를 추가해야 함
    
    
    
    // 수정할 제목/내용/블로그번호를 가진 BlogDto
    BlogDto blog = BlogDto.builder()
                    .title(title)
                    .contents(contents)
                    .blogNo(blogNo)
                    .build();
    
    // BLOG_T 수정
    int modifyResult = blogMapper.updateBlog(blog);
    
    return modifyResult;
    
  }
  
  @Override
  public int removeBlog(int blogNo) {
    
    //** 수정된 메소드 **//
    
    // BLOG_IMAGE_T 목록 가져와서 파일 삭제
    List<BlogImageDto> blogImageList = blogMapper.getBlogImageList(blogNo);
    for(BlogImageDto blogImage : blogImageList) {
      File file = new File(blogImage.getImagePath(), blogImage.getFilesystemName());
      if(file.exists()) {
        file.delete();
      }
    }
    
    // BLOG_IMAGE_T 삭제
    blogMapper.deleteBlogImage(blogNo);
    
    // BLOG_T 삭제
    return blogMapper.deleteBlog(blogNo);
    
  }
  
  
  @Override
  public Map<String, Object> addComment(HttpServletRequest request) {

    String contents = request.getParameter("contents");
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    int blogNo = Integer.parseInt(request.getParameter("blogNo"));
    
    CommentDto comment = CommentDto.builder()
                          .contents(contents)
                          .userDto(UserDto.builder()
                                    .userNo(userNo)
                                    .build())
                          .blogNo(blogNo)
                          .build();
    
    int addCommentResult = blogMapper.insertComment(comment);
    
    return Map.of("addCommentResult", addCommentResult);
    
  }

  @Transactional(readOnly=true)
  @Override
  public Map<String, Object> loadCommentList(HttpServletRequest request) {

    int blogNo = Integer.parseInt(request.getParameter("blogNo"));
    
    int page = Integer.parseInt(request.getParameter("page"));
    int total = blogMapper.getCommentCount(blogNo);
    int display = 10;
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("blogNo", blogNo
                                   , "begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd());
    
    List<CommentDto> commentList = blogMapper.getCommentList(map);
    String paging = myPageUtils.getAjaxPaging();
    
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("commentList", commentList);
    result.put("paging", paging);
    return result;
    
  }
  
  @Override
  public Map<String, Object> addCommentReply(HttpServletRequest request) {
    
    String contents = request.getParameter("contents");
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    int blogNo = Integer.parseInt(request.getParameter("blogNo"));
    int groupNo = Integer.parseInt(request.getParameter("groupNo"));
    
    CommentDto comment = CommentDto.builder()
                          .contents(contents)
                          .userDto(UserDto.builder()
                                    .userNo(userNo)
                                    .build())
                          .blogNo(blogNo)
                          .groupNo(groupNo)
                          .build();
    
    int addCommentReplyResult = blogMapper.insertCommentReply(comment);
    
    return Map.of("addCommentReplyResult", addCommentReplyResult);
    
  }
  
  @Override
  public Map<String, Object> removeComment(int commentNo) {
    int removeResult = blogMapper.deleteComment(commentNo);
    return Map.of("removeResult", removeResult);
  }
  
}
