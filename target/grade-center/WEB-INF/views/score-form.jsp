<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="score.edit"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
    <script src="${pageContext.request.contextPath}/assets/js/grade-check.js"></script>
</head>
<body>
<div class="layout">
    <jsp:include page="frame-nav.jsp"/>
    <main class="workspace">
        <div class="bar"><h1><fmt:message key="score.edit"/></h1></div>
        <form class="box" method="post" action="${pageContext.request.contextPath}/scores/save" onsubmit="return checkScoreForm()">
            <p class="error">${error}</p>
            <input type="hidden" name="id" value="${target.scoreId}">
            <label><fmt:message key="field.studentNo"/>
                <select name="studentId">
                    <c:forEach items="${students}" var="s">
                        <option value="${s.studentId}" ${target.studentId == s.studentId ? 'selected' : ''}>${s.studentNo} - ${s.fullName}</option>
                    </c:forEach>
                </select>
            </label>
            <label><fmt:message key="field.courseName"/>
                <select name="courseId">
                    <c:forEach items="${courses}" var="c">
                        <option value="${c.courseId}" ${target.courseId == c.courseId ? 'selected' : ''}>${c.courseCode} - ${c.courseName}</option>
                    </c:forEach>
                </select>
            </label>
            <label><fmt:message key="field.term"/><input name="termLabel" value="${empty target.termLabel ? '2026-春' : target.termLabel}" required></label>
            <label><fmt:message key="field.score"/><input name="scoreValue" type="number" min="0" max="100" step="0.01" value="${target.scoreValue}" required></label>
            <label><fmt:message key="field.remark"/><textarea name="remark">${target.remark}</textarea></label>
            <button class="button"><fmt:message key="btn.save"/></button>
        </form>
    </main>
</div>
</body>
</html>
