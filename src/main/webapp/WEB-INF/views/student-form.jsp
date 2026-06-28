<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="student.edit"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
    <script src="${pageContext.request.contextPath}/assets/js/grade-check.js"></script>
</head>
<body>
<div class="layout">
    <jsp:include page="frame-nav.jsp"/>
    <main class="workspace">
        <div class="bar"><h1><fmt:message key="student.edit"/></h1></div>
        <form class="box" method="post" action="${pageContext.request.contextPath}/students/save" onsubmit="return checkStudentForm()">
            <p class="error">${error}</p>
            <input type="hidden" name="id" value="${target.studentId}">
            <label><fmt:message key="field.id"/> / Account ID<input name="accountId" value="${target.accountId}"></label>
            <label><fmt:message key="field.studentNo"/><input name="studentNo" value="${target.studentNo}" required></label>
            <label><fmt:message key="field.name"/><input name="fullName" value="${target.fullName}" required></label>
            <label><fmt:message key="field.gender"/><select name="gender"><option ${target.gender == '女' ? 'selected' : ''}>女</option><option ${target.gender == '男' ? 'selected' : ''}>男</option></select></label>
            <label><fmt:message key="field.className"/><input name="className" value="${target.className}" required></label>
            <label><fmt:message key="field.year"/><input name="enrollmentYear" type="number" value="${empty target.enrollmentYear ? 2026 : target.enrollmentYear}" required></label>
            <button class="button"><fmt:message key="btn.save"/></button>
        </form>
    </main>
</div>
</body>
</html>
