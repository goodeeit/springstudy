<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="dt" value="<%=System.currentTimeMillis()%>" />

<jsp:include page="../layout/header.jsp">
  <jsp:param value="${upload.uploadNo}번 게시글" name="title"/>
</jsp:include>

<div>

  <h1 style="text-align: center;">Upload 게시글</h1>
  <div>작성자 : ${upload.userDto.name}</div>
  <div>작성일 : ${upload.createdAt}</div>
  <div>수정일 : ${upload.modifiedAt}</div>
  <div>제목 : ${upload.title}</div>
  <div>내용</div>
  <div>${upload.contents}</div>
  <div>
    <form id="frm_btn" method="post">
      <input type="hidden" name="uploadNo" value="${upload.uploadNo}">
      <button type="button" id="btn_edit">편집</button>
      <button type="button" id="btn_remove">삭제</button>
    </form>
  </div>
  
  <hr>
  
  <h4>첨부 다운로드</h4>
  <div>
    <c:if test="${empty attachList}">
      <div>첨부 없음</div>
    </c:if>
    <c:if test="${not empty attachList}">
      <c:forEach items="${attachList}" var="atc">
        <div>
          <c:if test="${atc.hasThumbnail == 1}">
            <img src="${contextPath}${atc.path}/s_${atc.filesystemName}" alt="썸네일" width="50px">
          </c:if>
          <c:if test="${atc.hasThumbnail == 0}">
            <img src="${contextPath}/resources/image/attach1.png" alt="썸네일" width="50px">
          </c:if>
          ${atc.originalFilename}
        </div>
      </c:forEach>
    </c:if>
  </div>
  
</div>
  
<script>

  const fnFileCheck = () => {
    $('#files').change((ev) => {
      $('#file_list').empty();
      let maxSize = 1024 * 1024 * 100;
      let maxSizePerFile = 1024 * 1024 * 10;
      let totalSize = 0;
      let files = ev.target.files;
      for(let i = 0; i < files.length; i++){
        totalSize += files[i].size;
        if(files[i].size > maxSizePerFile){
          alert('각 첨부파일의 최대 크기는 10MB입니다.');
          $(ev.target).val('');
          $('#file_list').empty();
          return;
        }
        $('#file_list').append('<div>' + files[i].name + '</div>');
      }
      if(totalSize > maxSize){
        alert('전체 첨부파일의 최대 크기는 100MB입니다.');
        $(ev.target).val('');
        $('#file_list').empty();
        return;
      }
    })
  }
  
  fnFileCheck();
  
</script>
  
<%@ include file="../layout/footer.jsp" %>