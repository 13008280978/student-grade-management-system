<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="signup.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
    <script src="${pageContext.request.contextPath}/assets/js/grade-check.js"></script>
</head>
<body class="login-page">
<form class="login-card" method="post" action="${pageContext.request.contextPath}/signup" onsubmit="return checkSignup()">
    <h2><fmt:message key="signup.title"/></h2>
    <p class="error">${error}</p>
    <label><fmt:message key="field.username"/><input name="loginName" required></label>
    <label><fmt:message key="field.password"/><input type="password" name="password" required></label>
    <label><fmt:message key="field.email"/><input name="mailAddress" required></label>
    <label><fmt:message key="field.mobile"/><input name="mobile" required></label>
    <label><fmt:message key="field.captcha"/>：${sessionScope.checkCode}<input name="checkCode" required></label>
    <button class="button"><fmt:message key="btn.register"/></button>
    <a class="button gray" href="${pageContext.request.contextPath}/signin"><fmt:message key="btn.backLogin"/></a>
</form>
</body>
</html>
