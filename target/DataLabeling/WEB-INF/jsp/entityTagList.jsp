<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<jsp:include page="/WEB-INF/jsp/taglib.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/entityTag.js"></script>
<title>Insert title here</title>
</head>
<body>
<input type="hidden" value="" id="classId"/>
<input type="hidden" value="${appId}" id="appId"/>
<input type="hidden" value="" id="classCode"/>
<div>
    <form style="float:left">
        <c:choose>
            <c:when test="${dataType eq 0 }">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="radio" name="data" value="0" checked onclick="changeType(this)"/>已标注数据
                &nbsp;&nbsp;&nbsp;&nbsp;
                <input type="radio" name="data" value="-1"  onclick="changeType(this)"/>未标注数据
            </c:when>
            <c:otherwise>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="radio" name="data" value="0" onclick="changeType(this)" />已标注数据
                &nbsp;&nbsp;&nbsp;&nbsp;
                <input type="radio" name="data" value="-1"  checked onclick="changeType(this)"/>未标注数据
            </c:otherwise>
        </c:choose>
    </form>
    <div style='clear:both'></div>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" >
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">添加类别标签</h4>
            </div>
            <div class="modal-body">
                <div class="dropdown" >
                    <div style="margin-left: 15%">
                        <span id="clickText">文本：这是一个文本对话</span>
                        <span id="clickId" style="display: none"></span>
                    </div>
                    <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" style="margin-left: 25%">
                        <span id="CCText">范本</span>
                        <span id="CCid" style="display: none"></span>
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="height:200px;overflow:scroll;margin-left: 25%" >
                        <c:forEach items="${pb.TClasses }" var="tClasses">
                            <li onclick="changeChoosed(this)" id="li${tClasses.id}" aria-checked="true"><a>${tClasses.recordClass}</a></li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" onclick="confimClick()">确定</button>
            </div>
        </div>
    </div>
</div>
<div class="col-md-9 col-lg-9 col-sm-9 col-xs-9">
    <table id="recordTable" class="table table-striped maintable" style="margin-bottom: 0">
    <tr><th>序号</th><th>文本</th><th>标记</th><th></th></tr>
<c:forEach var="info" items="${pageResource.list }" varStatus="status">
    <tr>
    <td>${ info.id}</td>
        <td > <div  id="record${info.id}">${info.resultRecord }</div></td>
        <td width="20%"><input class="btn btn-primary" onclick="getSelect('record${info.id}',${info.id})" type="button" value="标记"/>
            <input class="btn btn-primary" onclick="cancelSelect('record${info.id}',${info.id})" type="button" value="取消"/></td>
    </tr>
    <tr>
        <td></td>
        <td ><div id="record${info.id}Code">${info.resultCode}</div></td>
        <td></td>
    </tr>
</c:forEach>
</table>
    <c:if test="${dataType eq 0 }">
        <jsp:include page="page.jsp"></jsp:include>
    </c:if>

</div>
<div class="right-table  col-lg-3 col-md-3 col-sm-3 col-xs-3" >
            <div class="table-content" style="border: 1px solid #eee;height: 300px;overflow-y: scroll;overflow-x: hidden;">
                <table class="table table-hover similarTag " id="classTable">
                    <tr><th>#</th><th>实体前缀</th><th></th></tr>
                    <c:forEach items="${classList }" var="tClasses">
                        <tr draggable="true" ondragstart="drag(event)" id="sid${tClasses.id}">
                            <td onclick="chooseTag(${tClasses.id},'${tClasses.recordClass}',this)">${tClasses.id}</td>
                            <td onclick="chooseTag(${tClasses.id},'${tClasses.recordClass}',this)">${tClasses.recordClass}</td>
                            <td onclick="deleteTag(${tClasses.id},'${tClasses.recordClass}',this)">
                                <i class='glyphicon glyphicon-remove' ></i>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
    <button class="btn btn-primary" onclick="addClass()" style="margin-top: 20px;">增加类别标签</button>
    <div>
        <form name="form" action="fileUpload.action" method="post"  enctype="multipart/form-data" id="fileUpload">
            <input type="hidden" value="${appId}" name="appId">
            <input type="file" name="file">
            <input type="button" class="btn btn-primary uploadFile" onclick="uploadFile()" value="上传"/>
        </form>
    </div>
    <c:if test="${dataType eq 0 }">
      <input type="button" class="btn btn-primary" onclick="exportResult()" value="导出结果文本"/>
    </c:if>
</div>
</body>

</html>