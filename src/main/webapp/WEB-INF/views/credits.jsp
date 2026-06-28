<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="credits.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
</head>
<body>
<div class="layout">
    <jsp:include page="frame-nav.jsp"/>
    <main class="workspace">
        <div class="bar"><h1><fmt:message key="credits.title"/></h1></div>
        <form class="box grid-form" method="get">
            <label><fmt:message key="field.term"/><input name="term" value="${param.term}" placeholder="2026-春"></label>
            <button class="button"><fmt:message key="btn.search"/></button>
        </form>
        <table>
            <tr><th><fmt:message key="field.studentNo"/></th><th><fmt:message key="field.name"/></th><th><fmt:message key="field.passedCredit"/></th><th><fmt:message key="field.failedCredit"/></th><th><fmt:message key="field.weightedPoint"/></th><th><fmt:message key="field.completedCourses"/></th></tr>
            <c:forEach items="${audits}" var="a">
                <tr><td>${a.studentNo}</td><td>${a.fullName}</td><td>${a.passedCredit}</td><td>${a.failedCredit}</td><td>${a.weightedPoint}</td><td>${a.completedCourses}</td></tr>
            </c:forEach>
        </table>
    </main>
</div>
</body>
</html>
