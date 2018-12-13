<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
</head>
<script>
    // skin demo
    (function(){
        var _skin, _lhgcore;
        var _search = window.location.search;
        if (_search) {
            _skin = _search.split('demoSkin=')[1];
        };

        document.write('<scr'+'ipt src="${ctx }/js/lhgcore/lhgcore.lhgdialog.min.js?skin=' + (_skin || 'qq2011') +'"></sc'+'ript>');
        window._isDemoSkin = !!_skin;
    })();
</script>
<body>
<!-- 左侧菜单 -->
<div class="container" style="margin-top: 30px">
<div class="col-md-2">
    <ul class="nav nav-pills nav-stacked mynav" style="border:1px solid blanchedalmond">
        <li onclick="showMainPage(this)"><a href="${pageContext.request.contextPath}/welcome.jsp" target="mainframe" >Home</a></li>
        <c:forEach var="application" items="${applications}">
            <li id="${application.id}" onclick="showAppDetail(${application.id},this)"><a>${application.appName}</a></li>
        </c:forEach>
    </ul>
    <button class="btn bg-success" style="margin-top: 10px;" onclick="manageApplication()">所有标注项目管理</button>
    <button class="btn bg-success" style="margin-top: 10px;" onclick="addNewRecordClassProject(1)">新增分类标注项目</button>
    <button class="btn bg-success" style="margin-top: 10px;" onclick="addNewRecordClassProject(2)">新增相似标注1项目</button>
    <button class="btn bg-success" style="margin-top: 10px;" onclick="addNewRecordClassProject(3)">新增实体标注项目</button>
    <button class="btn bg-success" style="margin-top: 10px;" onclick="addNewRecordClassProject(4)">新增相似标注2项目</button>
</div>
<div class="col-md-10" id="main">
    <iframe name="mainframe" id="mainframe" style="width: 100%" src="${pageContext.request.contextPath}/welcome.jsp" frameborder="0" scrolling="no"  onload="iFrameHeight('mainframe')"></iframe>
</div>
</div>
</body>
<script type="text/javascript" src="${ctx }/js/main.js"></script>
</html>