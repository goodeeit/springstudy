<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="dt" value="<%=System.currentTimeMillis()%>" />

<jsp:include page="../layout/header.jsp">
  <jsp:param value="게시글작성" name="title"/>
</jsp:include>

<div>

  <form id="frm_free_add" method="post" action="${contextPath}/free/add.do">
    
    <h1 class="title">자유게시글 작성</h1>
    
    <div class="mb-3 row">
      <label for="email" class="col-sm-1 col-form-label">작성자</label>
      <div class="col-sm-11">
        <input type="text" readonly class="form-control-plaintext" id="email" name="email" value="${sessionScope.user.email}">
      </div>
    </div>
    <div class="input-group">
      <span class="input-group-text">내용</span>
      <textarea rows="5" name="contents" class="form-control"></textarea>
    </div>
    <div class="btn_wrap">
      <button type="submit" class="btn btn-secondary">작성완료</button>
    </div>
    
  </form>

</div>

<%@ include file="../layout/footer.jsp" %>