<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<link href="../css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
</head>
<body>
<!-- 左侧菜单 -->
<div>
<div class="sidebar" width="30%">
    <div class="nav-tt"><a href="javascript:void(0)" onclick="">快捷操作</a></div>
    <div class="subnav" style="height:570px"></div>
</div>

<!-- 主体 -->
<div class="content" id="container" width="70%">
    <iframe id="homecontentDiv" name="homecontentDiv" src="speechTag/list.action" width="99%" height="600px" frameborder="0" scrolling="no">
    </iframe>
</div>
</div>
</body>
</html>