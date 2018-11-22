<%--
  Created by IntelliJ IDEA.
  User: live800
  Date: 2018/10/16
  Time: 11:04
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<html>
<head>
    <title>访客记录相似标注</title>
    <link rel="stylesheet" type="text/css" href="${ctx }/css/listRecord.css">
</head>
<body>
<div>
    <form style="float:left">
        <c:choose>
            <c:when test="${pb.dataType eq 'notdeal' }">
                <div class="row">
                    <div class="col-lg-3 col-md-3 col-sm-3 col-xs-3" style="float: left;padding-left: 3%;">
                        <input type="radio" name="data" value="dealed" onclick="getUrl('dataType','dealed')">已标注数据&nbsp;&nbsp;
                        <input type="radio" name="data" value="notdeal" checked onclick="getUrl('dataType','notdeal')">未标注数据
                    </div>
                    <div class="input-group col-lg-3 col-md-3 col-sm-3 col-xs-3"  style="line-height: 33px;margin-left: 48%">
                        <input type="text" class="form-control inleKeyword" placeholder="搜索" aria-describedby="basic-addon3" value="${pb.noHandledWord}">
                        <span class="input-group-addon iparent" id="basic-addon3" ><i class="glyphicon glyphicon-search" onclick="getUrl('noHandledWord',this)"></i></span>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                &nbsp;&nbsp;
                <input type="radio" name="data" value="dealed" onclick="getUrl('dataType','dealed')" checked style="padding-left: 3%;">已标注数据
                &nbsp;&nbsp;&nbsp;&nbsp;
                <input type="radio" name="data" value="notdeal"  onclick="getUrl('dataType','notdeal')">未标注数据
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
                <h4 class="modal-title" id="myModalLabel">添加相似记录范本</h4>
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
                    <ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="height:200px;overflow:scroll;margin-left: 25%">
                        <c:forEach items="${pb.TClasses }" var="tclass">
                            <li onclick="changeChoosed(this)" id="li${tclass.id}" aria-checked="true"><a>${tclass.recordClass}</a></li>
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
    <table class="table table-striped maintable" style="margin-bottom: 0">
        <tr><th>序号</th><th>访客记录文本</th><th>相似记录范本</th><th></th></tr>
        <c:choose>
            <c:when test="${pb.dataType eq 'notdeal' }">
                <c:forEach items="${SSData[pb.appId]}" var="recordInfo">
                    <tr>
                        <td>${recordInfo.id }</td>
                        <td>${recordInfo.chatRecord }</td>
                        <c:choose>
                            <c:when test="${recordInfo.recordClass eq null}">
                                <td class="sclass" ondrop="drop_handler(event);" ondragover="dragover_handler(event);"><input type="number" placeholder="请输入编号"/></td>
                                <td><i class='glyphicon glyphicon-plus' id='rr${recordInfo.id}' data-toggle='modal' data-target='#myModal'></i></td>
                            </c:when>
                            <c:otherwise>
                                <td class="sclass" ondrop="drop_handler(event);" ondragover="dragover_handler(event);">${recordInfo.recordClass }</td>
                                <td><i class='glyphicon glyphicon-remove' onclick="javascript:similarTagRemove(${recordInfo.id },this)"></i></td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <c:forEach items="${pb.beanListUp}" var="recordInfo">
                    <tr>
                        <td>${recordInfo.id }</td>
                        <td>${recordInfo.chatRecord }</td>
                        <c:choose>
                            <c:when test="${recordInfo.recordClass eq null}">
                                <td class="sclass" ondrop="drop_handler(event);" ondragover="dragover_handler(event);"><input type="number" placeholder="请输入编号"/></td>
                                <td><i class='glyphicon glyphicon-plus' id='rr${recordInfo.id}' data-toggle='modal' data-target='#myModal'></i></td>
                            </c:when>
                            <c:otherwise>
                                <td class="sclass" ondrop="drop_handler(event);" ondragover="dragover_handler(event);">${recordInfo.recordClass }</td>
                                <td><i class='glyphicon glyphicon-remove' onclick="javascript:similarTagRemove(${recordInfo.id },this)"></i></td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </table>
    <jsp:include page="page.jsp"/>
    <c:choose>
        <c:when test="${pb.beanListDown eq null}">
            <c:choose>
                <c:when test="${pb.dataType eq 'dealed'}">
                    <div>
                        <button class="btn btn-info" style="margin-top: 10px;" onclick="downLoadData(1)">导出当前选择的相似记录范本</button>
                        <button class="btn btn-info" style="margin-top: 10px;" onclick="downLoadData(2)">导出当前搜索出的所有相似记录范本</button>
                        <button class="btn btn-info" style="margin-top: 10px;" onclick="downLoadData(3)">导出全部标注数据</button>
                    </div>
                </c:when>
                <c:otherwise>
                    <div></div>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <div>
                <table class="table table-bordered downTable">
                    <tr><th>序号</th><th>访客记录文本</th></tr>
                    <c:forEach items="${pb.beanListDown }" var="recordInfo">
                        <tr >
                            <td>${recordInfo.id }</td>
                            <td>${recordInfo.chatRecord }</td>
                        </tr>
                    </c:forEach>
                </table>
                <div class="alert alert-info" role="alert" style="text-align: center;margin-bottom: 0;line-height: 0;height: 20px;">${pb.TClass.id}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${pb.TClass.recordClass}</div>

                <jsp:include page="page1.jsp"/>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<div class="right-table col-lg-3 col-md-3 col-sm-3 col-xs-3">
    <div class="input-group">
        <input type="text" class="form-control inKeyword" placeholder="搜索" aria-describedby="basic-addon2" value="${pb.keyword}">
        <span class="input-group-addon iparent" id="basic-addon2" ><i class="glyphicon glyphicon-search" onclick="getUrl('keyword',this)"></i></span>
    </div>
    <div class="table-content" style="border: 1px solid #eee;height: 300px;overflow-y: scroll;overflow-x: hidden">
        <table class="table table-hover similarTag">
            <tr><th>#</th><th>相似记录范本</th><th></th></tr>
            <c:forEach items="${pb.TClasses }" var="tclass">
                <tr draggable="true" ondragstart="drag(event)" id="sid${tclass.id}">
                    <td onclick="getUrl('clickwordId',${tclass.id})">${tclass.id}</td>
                    <td onclick="getUrl('clickwordId',${tclass.id})" style="margin-bottom: 0;table-layout: fixed;word-break: break-all;">${tclass.recordClass}</td>
                    <td onclick="similarTagDelete(${tclass.id},'${tclass.recordClass}')">
                        <i class='glyphicon glyphicon-remove' ></i>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <button class="btn btn-primary" onclick="addClass()" style="margin-top: 20px;">增加相似记录范本</button>
    </br><input id="file" type="file" style="margin-top: 20px;"/><br/>
    <input class="btn btn-primary uploadFile" type="submit" value="上传">
</div>
</body>
<script type="application/javascript" src="${ctx }/js/listSimilar.js"></script>
<script type="text/javascript" src='${ctx }/js/bootstrap.min.js'></script>
</html>
