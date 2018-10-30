<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="ctx/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<%--<link rel="stylesheet" href="<c:url value='/css/zd_layout.css'/>"/>
<link rel="stylesheet" href="<c:url value='/css/zd_base.css'/>"/>
<link rel="stylesheet" href="<c:url value='/css/css.css'/>"/>
<link href="<c:url value='/css/bootstrap.min.css'/>" rel="stylesheet" type="text/css" />
<link href="<c:url value='/css/validate/tips.css'/>" rel="stylesheet" type="text/css" />
<script src="<c:url value='/js/jquery-1.7.2.js'/>"></script>
<script src="<c:url value='/js/jquery.min.js'/>"></script>--%>
<script type="text/javascript">
var path='<%=basePath%>';
</script>

