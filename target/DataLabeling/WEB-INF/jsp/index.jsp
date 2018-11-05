<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: live800
  Date: 2018/10/30
  Time: 11:46
  To change this template use File | Settings | File Templates.
--%>

<%@ page pageEncoding="UTF-8"%>
<html>
<jsp:include page="taglib.jsp"/>
<head>
    <title>数据标注系统</title>
    <style type="text/css">
        table tr td:first-child,td:last-child{
            width: 15%;
        }
        table tr td:nth-of-type(2){
            width: 70%;
        }
    </style>
</head>
<body>
<div class="container" style="margin-top: 30px">
    <div class="row">
        <h3 class='col-md-6 col-md-offset-3' style="text-align: center">数据标注系统</h3>
    </div>
    <br/><br/>
    <div class="col-md-6 col-md-offset-3">
        <table class="table table-striped" style="margin-bottom: 0">
            <tr><th>序号</th><th>项目名</th></tr>
            <c:forEach items="${projectList }" var="project">
                <tr onclick="clickToThisPage(${project.id })">
                    <td>${project.id }</td>
                    <td>${project.projectName }</td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div class="col-md-6 col-md-offset-3" style="text-align: right;margin-top: 3%;">
        <button class="btn btn-primary" onclick="addProject()">新增项目</button>
    </div>
</div>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery-3.1.1.min.js'></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/index.js"></script>
</body>
</html>
