<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="signin.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
</head>
<body class="login-page">
<form class="login-card" method="post" action="${pageContext.request.contextPath}/signin">
    <h2><fmt:message key="app.title"/></h2>
    <p class="error">${error}</p>
    <label><fmt:message key="field.username"/><input name="loginName" required></label>
    <label><fmt:message key="field.password"/><input type="password" name="password" required></label>
    <label><fmt:message key="field.captcha"/>：${sessionScope.checkCode}<input name="checkCode" required></label>
    <button class="button"><fmt:message key="btn.login"/></button>
    <a class="button gray" href="${pageContext.request.contextPath}/signup"><fmt:message key="btn.register"/></a>
    <a class="button gray" href="${pageContext.request.contextPath}/language?to=zh"><fmt:message key="menu.zh"/></a>
    <a class="button gray" href="${pageContext.request.contextPath}/language?to=en"><fmt:message key="menu.en"/></a>
</form>
</body>
</html>
