<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="program.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
</head>
<body>
<div class="layout">
    <jsp:include page="frame-nav.jsp"/>
    <main class="workspace">
        <div class="bar"><h1><fmt:message key="program.title"/></h1></div>
        <form class="box grid-form" method="get">
            <label><fmt:message key="field.requiredCredit"/><input name="requiredCredit" type="number" step="0.1" value="${empty param.requiredCredit ? 12 : param.requiredCredit}"></label>
            <button class="button"><fmt:message key="btn.search"/></button>
        </form>
        <table>
            <tr><th><fmt:message key="field.className"/></th><th><fmt:message key="home.students"/></th><th><fmt:message key="field.requiredCredit"/></th><th><fmt:message key="field.avgPassedCredit"/></th><th><fmt:message key="field.completionRate"/></th><th><fmt:message key="field.coachingFocus"/></th></tr>
            <c:forEach items="${progressRows}" var="p">
                <tr><td>${p.className}</td><td>${p.studentTotal}</td><td>${p.requiredCredit}</td><td>${p.averagePassedCredit}</td><td>${p.completionRate}%</td><td>${p.coachingFocus}</td></tr>
            </c:forEach>
        </table>
    </main>
</div>
</body>
</html>
