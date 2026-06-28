<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="alerts.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
</head>
<body>
<div class="layout">
    <jsp:include page="frame-nav.jsp"/>
    <main class="workspace">
        <div class="bar"><h1><fmt:message key="alerts.title"/></h1></div>
        <form class="box grid-form" method="get">
            <label><fmt:message key="field.term"/><input name="term" value="${param.term}" placeholder="2026-春"></label>
            <button class="button"><fmt:message key="btn.search"/></button>
        </form>
        <table>
            <tr><th><fmt:message key="field.studentNo"/></th><th><fmt:message key="field.name"/></th><th><fmt:message key="field.courseName"/></th><th><fmt:message key="field.term"/></th><th><fmt:message key="field.score"/></th><th><fmt:message key="alerts.type"/></th><th><fmt:message key="field.advice"/></th></tr>
            <c:forEach items="${alerts}" var="a">
                <tr><td>${a.studentNo}</td><td>${a.studentName}</td><td>${a.courseName}</td><td>${a.termLabel}</td><td>${a.scoreValue}</td><td>${a.alertType}</td><td>${a.advice}</td></tr>
            </c:forEach>
        </table>
    </main>
</div>
</body>
</html>
