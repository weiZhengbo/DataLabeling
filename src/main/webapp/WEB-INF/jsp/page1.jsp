<%--
  Created by IntelliJ IDEA.
  User: live800
  Date: 2018/11/5
  Time: 13:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="taglib.jsp"%>

<nav aria-label="Page navigation">
    <c:choose>
        <%-- 第一条：如果总页数<=10，那么页码列表为1 ~ tp --%>
        <c:when test="${pb.tp1 <= 8 }">
            <c:set var="begin" value="1"/>
            <c:set var="end" value="${pb.tp1 }"/>
        </c:when>
        <c:otherwise>
            <%-- 第二条：按公式计算，让列表的头为当前页-4；列表的尾为当前页+5 --%>
            <c:set var="begin" value="${pb.pc-3 }"/>
            <c:set var="end" value="${pb.pc+5 }"/>
            <%-- 第三条：第二条只适合在中间，而两端会出问题。这里处理begin出界！ --%>
            <%-- 如果begin<1，那么让begin=1，相应end=10 --%>
            <c:if test="${begin<1 }">
                <c:set var="begin" value="1"/>
                <c:set var="end" value="8"/>
            </c:if>
            <%-- 第四条：处理end出界。如果end>tp，那么让end=tp，相应begin=tp-9 --%>
            <c:if test="${end>pb.tp1 }">
                <c:set var="begin" value="${pb.tp1-7 }"/>
                <c:set var="end" value="${pb.tp1 }"/>
            </c:if>
        </c:otherwise>
    </c:choose>
    <ul class="pagination">
        <c:if test="${pb.pc > 1 }">
            <li >
                <a href = "javascript:void(0);" onclick="getUrl('pc',${pb.pc-1})" aria-label="Previous" >
                    <span aria-hidden="true" >&laquo;</span>
                </a>
            </li>
        </c:if>
        <c:if test="${pb.pc <= 1 }">
            <li class="disabled">
                <a href="#" aria-label="Previous" >
                    <span aria-hidden="true" >&laquo;</span>
                </a>
            </li>
        </c:if>

        <c:forEach begin="${begin }" end="${end }" var="i">
            <c:choose>
                <c:when test="${i eq pb.pc }"><li class="active"><a href="#">${i }</a></li></c:when>
                <c:otherwise>
                    <li><a href = "javascript:void(0);" onclick="getUrl('pc',${i })">${i }</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:if test="${pb.pc < pb.tp1 }">
            <li >
                <a href = "javascript:void(0);" onclick="getUrl('pc',${pb.pc+1})" aria-label="Next" >
                    <span aria-hidden="true" >&raquo;</span>
                </a>
            </li>
        </c:if>
        <c:if test="${pb.pc >= pb.tp1 }">
            <li class="disabled">
                <a href="#" aria-label="Previous" >
                    <span aria-hidden="true" >&raquo;</span>
                </a>
            </li>
        </c:if>
    </ul>
</nav>