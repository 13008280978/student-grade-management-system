<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="scholarship.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
</head>
<body>
<div class="layout">
    <jsp:include page="frame-nav.jsp"/>
    <main class="workspace">
        <div class="bar"><h1><fmt:message key="scholarship.title"/></h1></div>
        <form class="box grid-form" method="get">
            <label><fmt:message key="field.term"/><input name="term" value="${param.term}" placeholder="2026-春"></label>
            <label><fmt:message key="scholarship.min"/><input name="minAverage" type="number" step="0.01" value="${empty param.minAverage ? 85 : param.minAverage}"></label>
            <button class="button"><fmt:message key="btn.search"/></button>
        </form>
        <table>
            <tr><th><fmt:message key="field.studentNo"/></th><th><fmt:message key="field.name"/></th><th><fmt:message key="field.className"/></th><th><fmt:message key="home.average"/></th><th><fmt:message key="field.excellent"/></th><th><fmt:message key="field.failed"/></th><th><fmt:message key="field.eligibility"/></th></tr>
            <c:forEach items="${candidates}" var="c">
                <tr><td>${c.studentNo}</td><td>${c.fullName}</td><td>${c.className}</td><td>${c.averageScore}</td><td>${c.excellentCourses}</td><td>${c.failedCourses}</td><td>${c.eligibility}</td></tr>
            </c:forEach>
        </table>
    </main>
</div>
</body>
</html>
