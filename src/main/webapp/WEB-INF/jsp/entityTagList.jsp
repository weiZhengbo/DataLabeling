<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="/WEB-INF/jsp/taglib.jsp" flush="true"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<div class="table-responsive">
<table class="table table-striped maintable" style="margin-bottom: 0">
    <tr><th>序号</th><th>文本</th><th>标记</th><th></th></tr>
    <tr>
<c:forEach var="info" items="${recordInfoList }" varStatus="status">
    <td>${ status.index + 1}</td>
    <td>${info.chatRecord }</td>
    <td><a>标记</a>/td>
</c:forEach>
    <td>
</table>
</div>
</body>
</html>