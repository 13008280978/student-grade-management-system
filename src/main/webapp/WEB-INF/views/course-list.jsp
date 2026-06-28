<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="course.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
    <script src="${pageContext.request.contextPath}/assets/js/grade-check.js"></script>
</head>
<body>
<div class="layout">
    <jsp:include page="frame-nav.jsp"/>
    <main class="workspace">
        <div class="bar"><h1><fmt:message key="course.title"/></h1><c:if test="${sessionScope.signedAccount.roleCode == 'manager'}"><a class="button" href="${pageContext.request.contextPath}/courses/new"><fmt:message key="btn.new"/></a></c:if></div>
        <form class="box grid-form" method="get">
            <label><fmt:message key="btn.search"/><input name="keyword" value="${param.keyword}"></label>
            <button class="button"><fmt:message key="btn.search"/></button>
        </form>
        <table>
            <tr><th><fmt:message key="field.courseCode"/></th><th><fmt:message key="field.courseName"/></th><th><fmt:message key="field.credit"/></th><th><fmt:message key="field.teacher"/></th><c:if test="${sessionScope.signedAccount.roleCode == 'manager'}"><th><fmt:message key="field.action"/></th></c:if></tr>
            <c:forEach items="${courses}" var="c">
                <tr>
                    <td>${c.courseCode}</td><td>${c.courseName}</td><td>${c.credit}</td><td>${c.teacherName}</td>
                    <c:if test="${sessionScope.signedAccount.roleCode == 'manager'}">
                        <td><a class="button" href="${pageContext.request.contextPath}/courses/edit?id=${c.courseId}"><fmt:message key="btn.edit"/></a>
                            <form method="post" action="${pageContext.request.contextPath}/courses/delete" style="display:inline" onsubmit="return ask('确认删除？')"><input type="hidden" name="id" value="${c.courseId}"><button class="button red"><fmt:message key="btn.delete"/></button></form></td>
                    </c:if>
                </tr>
            </c:forEach>
        </table>
    </main>
</div>
</body>
</html>
