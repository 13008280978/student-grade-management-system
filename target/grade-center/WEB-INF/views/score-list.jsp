<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="score.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
    <script src="${pageContext.request.contextPath}/assets/js/grade-check.js"></script>
</head>
<body>
<div class="layout">
    <jsp:include page="frame-nav.jsp"/>
    <main class="workspace">
        <div class="bar"><h1><fmt:message key="score.title"/></h1><c:if test="${sessionScope.signedAccount.roleCode == 'manager'}"><a class="button" href="${pageContext.request.contextPath}/scores/new"><fmt:message key="btn.new"/></a></c:if></div>
        <form class="box grid-form" method="get">
            <label><fmt:message key="field.term"/><input name="term" value="${param.term}"></label>
            <label><fmt:message key="btn.search"/><input name="keyword" value="${param.keyword}"></label>
            <button class="button"><fmt:message key="btn.search"/></button>
        </form>
        <table>
            <tr><th><fmt:message key="field.studentNo"/></th><th><fmt:message key="field.name"/></th><th><fmt:message key="field.courseName"/></th><th><fmt:message key="field.term"/></th><th><fmt:message key="field.score"/></th><th><fmt:message key="field.remark"/></th><c:if test="${sessionScope.signedAccount.roleCode == 'manager'}"><th><fmt:message key="field.action"/></th></c:if></tr>
            <c:forEach items="${scores}" var="s">
                <tr>
                    <td>${s.studentNo}</td><td>${s.studentName}</td><td>${s.courseName}</td><td>${s.termLabel}</td><td>${s.scoreValue}</td><td>${s.remark}</td>
                    <c:if test="${sessionScope.signedAccount.roleCode == 'manager'}">
                        <td><a class="button" href="${pageContext.request.contextPath}/scores/edit?id=${s.scoreId}"><fmt:message key="btn.edit"/></a>
                            <form method="post" action="${pageContext.request.contextPath}/scores/delete" style="display:inline" onsubmit="return ask('确认删除？')"><input type="hidden" name="id" value="${s.scoreId}"><button class="button red"><fmt:message key="btn.delete"/></button></form></td>
                    </c:if>
                </tr>
            </c:forEach>
        </table>
    </main>
</div>
</body>
</html>
