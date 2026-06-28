<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="student.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
    <script src="${pageContext.request.contextPath}/assets/js/grade-check.js"></script>
</head>
<body>
<div class="layout">
    <jsp:include page="frame-nav.jsp"/>
    <main class="workspace">
        <div class="bar"><h1><fmt:message key="student.title"/></h1><c:if test="${sessionScope.signedAccount.roleCode == 'manager'}"><a class="button" href="${pageContext.request.contextPath}/students/new"><fmt:message key="btn.new"/></a></c:if></div>
        <form class="box grid-form" method="get">
            <label><fmt:message key="btn.search"/><input name="q" value="${param.q}"></label>
            <button class="button"><fmt:message key="btn.search"/></button>
        </form>
        <table>
            <tr><th><fmt:message key="field.studentNo"/></th><th><fmt:message key="field.name"/></th><th><fmt:message key="field.gender"/></th><th><fmt:message key="field.className"/></th><th><fmt:message key="field.year"/></th><th><fmt:message key="field.status"/></th><c:if test="${sessionScope.signedAccount.roleCode == 'manager'}"><th><fmt:message key="field.action"/></th></c:if></tr>
            <c:forEach items="${students}" var="s">
                <tr>
                    <td>${s.studentNo}</td><td>${s.fullName}</td><td>${s.gender}</td><td>${s.className}</td><td>${s.enrollmentYear}</td><td>${s.archived == 0 ? '在籍' : '归档'}</td>
                    <c:if test="${sessionScope.signedAccount.roleCode == 'manager'}">
                        <td>
                            <a class="button" href="${pageContext.request.contextPath}/students/edit?id=${s.studentId}"><fmt:message key="btn.edit"/></a>
                            <form method="post" action="${pageContext.request.contextPath}/students/archive" style="display:inline" onsubmit="return ask('确认归档？')"><input type="hidden" name="id" value="${s.studentId}"><button class="button gray"><fmt:message key="btn.disable"/></button></form>
                            <form method="post" action="${pageContext.request.contextPath}/students/delete" style="display:inline" onsubmit="return ask('确认删除？')"><input type="hidden" name="id" value="${s.studentId}"><button class="button red"><fmt:message key="btn.delete"/></button></form>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
        </table>
    </main>
</div>
</body>
</html>
