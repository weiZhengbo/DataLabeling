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
	<title>短文本相似标注2</title>
	<link rel="stylesheet" type="text/css" href="${ctx }/css/jquery.mloading.css"/>
</head>
<body onbeforeunload="return leave()">
<div>
	<form style="float:left">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input class="check" type="radio" name="data" value="dealed" onclick="chagnePage('dealed')"/>已标注数据
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input class="check" type="radio" name="data" value="notdeal" onclick="chagnePage('notdeal')"/>未标注数据
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input class="check" type="radio" name="data" value="all"  onclick="chagnePage('all')"/>全部数据
			&nbsp;&nbsp;&nbsp;&nbsp;
	</form>
	<div>
		<div class="col-xs-8 col-sm-3 col-md-3 col-sm-offset-1">
			<div class="file-container" style="display:inline-block;position:relative;overflow: hidden;vertical-align:middle"> 
				<button class="btn btn-success fileinput-button" type="button">选择文件</button>
				<input type="file" id="file" onchange="loadFile(this.files[0])" style="position:absolute;top:0;left:0;font-size:34px; opacity:0;width: 180px;">
			</div>
			<span id="filename" style="vertical-align: middle;display: inline-block;width: 45%;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">未上传文件</span>
		</div>
		<button class="btn btn-primary uploadFile" onclick="uploadFile()"  >上传</button>
		<button class="btn btn-primary confirmFlag" style="margin-left: 80px;" onclick="confirmFlag()" >确认本页已标记</button><br/>
	</div>

	<div style='clear:both'></div>
</div>

<div class="col-md-12 col-lg-12 col-sm-12 col-xs-12">
	<table class="table table-striped maintable" style="margin-bottom: 0">
		<tr><th width="10%">序号</th><th width="10%">类型</th><th width="35%">文本1</th><th width="35%">文本2</th><th width="10%">是否相似</th></tr>
		<c:forEach items="${pb.TClasses}" var="record">
		<tr>
			<td class="recordid">${record.id}</td>
			<td>${record.type}</td>
			<td>${record.visit_ques}</td>
			<td>${record.match_ques}</td>
			<td>
				<c:choose>
					<c:when test="${pb.dataType eq 'all'}">
						${record.isSimilar}
					</c:when>
					<c:otherwise>
						<input value="${record.isSimilar}" style="width: 100%"/>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		</c:forEach>
	</table>
	<jsp:include page="page.jsp"/>

	<div>
		<span style="font-size: 15;color: red;">**相似标记为1，不相似标记为0</span>
	</div>
	<div>
		<button class="btn btn-info" style="margin-top: 10px;" onclick="downLoadData(1)">导出已标注数据</button>
	</div>
</div>
</body>
<script>
    $(document).ready(function () {
        var type = '${pb.dataType}';
        if (type!='notdeal'){
            $('.confirmFlag').hide();
        }
        $('.check').each(function () {
            if($(this).val()==type){
                $(this).attr('checked','true');
            }
        })
    })
    function leave() {
        var _list = [];
        var i=0;
        var type = '${pb.dataType}';
        if (type=='notdeal'){
            $('.recordid').each(function () {
                _list[i++]=parseInt($(this).text());
            });
            var url1  = document.URL;
            var head1 = url1.substring(0,url1.lastIndexOf('/'));
            var head = head1.substring(0,head1.lastIndexOf('/'));
            $.ajax({
                type: "POST",
                url:head+"/SimilarPair/updateSync",
                data:"list="+_list,
                success: function(data) {
                }
            });
        }
    }
    function confirmFlag() {
        var _list = [];
        var i=0;
        $('.recordid').each(function () {
            _list[i++]=parseInt($(this).text());
        });
        var url1  = document.URL;
        var head1 = url1.substring(0,url1.lastIndexOf('/'));
        var head = head1.substring(0,head1.lastIndexOf('/'));
        $.ajax({
            type: "POST",
            url:head+"/SimilarPair/updateFlag",
            data:"list="+_list,
            success: function(data) {
            }
        });
    }
</script>
<script type="text/javascript" src="${ctx }/js/jquery.mloading.js"></script>
<script type="text/javascript" src="${ctx }/js/listSimilarPair.js"></script>
<script type="text/javascript" src='${ctx }/js/bootstrap.min.js'></script>
</html>
