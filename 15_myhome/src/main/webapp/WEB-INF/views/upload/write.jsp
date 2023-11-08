<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="dt" value="<%=System.currentTimeMillis()%>" />

<jsp:include page="../layout/header.jsp">
  <jsp:param value="업로드게시글작성" name="title"/>
</jsp:include>

<div>

  <h1 class="title">Upload 게시글 작성하기</h1>
  
  <form id="frm_upload_add" method="post" action="${contextPath}/upload/add.do" enctype="multipart/form-data">
    <div>
      <label for="email" class="form-label">작성자</label>
      <input type="text" id="email" class="form-control-plaintext" value="${sessionScope.user.email}" readonly>
    </div>
    <div>
      <label for="title" class="form-label">제목</label>
      <input type="text" name="title" id="title" class="form-control">
    </div>
    <div>
      <label for="contents" class="form-label">내용</label>
      <textarea rows="3" name="contents" id="contents" class="form-control"></textarea>
    </div>
    <div>
      <label for="files" class="form-label">첨부</label>
      <input type="file" name="files" id="files" class="form-control" multiple>
    </div>
    <div class="attached_list" id="attached_list"></div>
    <div class="btn_wrap">
      <input type="hidden" name="userNo" value="${sessionScope.user.userNo}">
      <button type="submit" class="btn btn-primary">작성완료</button>
    </div>
  </form>
  
</div>
  
<script>

  const fnFileCheck = () => {
    $('#files').change((ev) => {
      $('#attached_list').empty();
      let maxSize = 1024 * 1024 * 100;
      let maxSizePerFile = 1024 * 1024 * 10;
      let totalSize = 0;
      let files = ev.target.files;
      for(let i = 0; i < files.length; i++){
        totalSize += files[i].size;
        if(files[i].size > maxSizePerFile){
          alert('각 첨부파일의 최대 크기는 10MB입니다.');
          $(ev.target).val('');
          $('#attached_list').empty();
          return;
        }
        $('#attached_list').append('<div>' + files[i].name + '</div>');
      }
      if(totalSize > maxSize){
        alert('전체 첨부파일의 최대 크기는 100MB입니다.');
        $(ev.target).val('');
        $('#attached_list').empty();
        return;
      }
    })
  }
  
  const fnSubmit = () => {
	  $('#frm_upload_add').submit((ev) => {
		  if($('#title').val() === ''){
			  alert('제목은 반드시 입력해야 합니다.');
			  ev.preventDefault();
			  return;
		  }
	  })
  }
  
  fnFileCheck();
  fnSubmit();
  
</script>
  
<%@ include file="../layout/footer.jsp" %>