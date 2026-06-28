<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="course.edit"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
</head>
<body>
<div class="layout">
    <jsp:include page="frame-nav.jsp"/>
    <main class="workspace">
        <div class="bar"><h1><fmt:message key="course.edit"/></h1></div>
        <form class="box" method="post" action="${pageContext.request.contextPath}/courses/save">
            <p class="error">${error}</p>
            <input type="hidden" name="id" value="${target.courseId}">
            <label><fmt:message key="field.courseCode"/><input name="courseCode" value="${target.courseCode}" required></label>
            <label><fmt:message key="field.courseName"/><input name="courseName" value="${target.courseName}" required></label>
            <label><fmt:message key="field.credit"/><input name="credit" type="number" step="0.1" value="${target.credit}" required></label>
            <label><fmt:message key="field.teacher"/><input name="teacherName" value="${target.teacherName}" required></label>
            <button class="button"><fmt:message key="btn.save"/></button>
        </form>
    </main>
</div>
</body>
</html>
