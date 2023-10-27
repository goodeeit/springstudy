<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
</head>
<body>

  <h1>상세보기</h1>
  
  <div>${bbs.bbsNo}</div>
  <div>${bbs.editor}</div>
  <div>${bbs.title}</div>
  <div>${bbs.contents}</div>
  <div>${bbs.createdAt}</div>
  <div>${bbs.modifiedAt}</div>

</body>
</html>