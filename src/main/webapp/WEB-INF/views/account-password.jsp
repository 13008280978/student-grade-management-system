<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="account.password"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
</head>
<body>
<div class="layout">
    <jsp:include page="frame-nav.jsp"/>
    <main class="workspace">
        <div class="bar"><h1><fmt:message key="account.password"/></h1></div>
        <form class="box" method="post" action="${pageContext.request.contextPath}/accounts/password">
            <p class="error">${error}</p>
            <input type="hidden" name="id" value="${id}">
            <label><fmt:message key="account.newPassword"/><input type="password" name="newPassword" required></label>
            <label><fmt:message key="account.repeatPassword"/><input type="password" name="repeatPassword" required></label>
            <label><fmt:message key="account.managerPassword"/><input type="password" name="managerPassword" required></label>
            <button class="button"><fmt:message key="btn.save"/></button>
        </form>
    </main>
</div>
</body>
</html>
