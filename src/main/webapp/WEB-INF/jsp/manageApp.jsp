<%--
  Created by IntelliJ IDEA.
  User: live800
  Date: 2018/11/23
  Time: 10:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>标注项目管理</title>
<html>
<body>
    <div class="col-md-12 col-lg-12 col-sm-12 col-xs-12">
        <table id="recordTable" class="table table-striped maintable" style="margin-bottom: 0;table-layout: fixed;word-break: break-all;">
            <tr><th width="20%">序号</th><th width="50%">项目名称</th><th width="30%">操作</th></tr>
            <c:forEach var="info" items="${applications }" varStatus="status">
                <tr>
                    <td>${ info.id}</td>
                    <td > <span  id="appName${info.id}">${info.appName }</span></td>
                    <td ><input class="btn btn-primary" onclick="editApp('appName${info.id}',${info.id})" type="button" value="修改"/>
                        <input class="btn btn-primary" onclick="delApp(${info.id},'${info.appName }')" type="button" value="删除"/></td>
                </tr>
            </c:forEach>
        </table>
    </div>
</body>
<script type="text/javascript" src="${ctx }/js/main.js"></script>
</html>
