<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="transcript.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
</head>
<body>
<div class="layout">
    <jsp:include page="frame-nav.jsp"/>
    <main class="workspace">
        <div class="bar"><h1><fmt:message key="transcript.title"/></h1><span>${sessionScope.signedAccount.loginName}</span></div>
        <form class="box grid-form" method="get">
            <label><fmt:message key="field.term"/><input name="term" value="${param.term}"></label>
            <label><fmt:message key="btn.search"/><input name="keyword" value="${param.keyword}"></label>
            <button class="button"><fmt:message key="btn.search"/></button>
        </form>
        <section class="box transcript-note"><fmt:message key="transcript.tip"/></section>
        <table>
            <tr><th><fmt:message key="field.studentNo"/></th><th><fmt:message key="field.name"/></th><th><fmt:message key="field.courseName"/></th><th><fmt:message key="field.term"/></th><th><fmt:message key="field.score"/></th><th><fmt:message key="field.gpa"/></th><th><fmt:message key="field.level"/></th></tr>
            <c:forEach items="${scores}" var="s">
                <tr><td>${s.studentNo}</td><td>${s.studentName}</td><td>${s.courseName}</td><td>${s.termLabel}</td><td>${s.scoreValue}</td><td>${gpa[s.scoreId]}</td><td>${level[s.scoreId]}</td></tr>
            </c:forEach>
        </table>
    </main>
</div>
</body>
</html>
