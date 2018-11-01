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
<form name="form" action="fileUpload.action" method="post"  enctype="multipart/form-data" >
    <input type="hidden" value="-1" name="appFlag">
    <input type="file" name="file">
    <input type="submit" value="upload"/>
</form>
</body>
</html>