<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="account.edit"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
</head>
<body>
<div class="layout">
    <jsp:include page="frame-nav.jsp"/>
    <main class="workspace">
        <div class="bar"><h1><fmt:message key="account.edit"/></h1></div>
        <form class="box" method="post" action="${pageContext.request.contextPath}/accounts/edit">
            <p class="error">${error}</p>
            <input type="hidden" name="id" value="${target.accountId}">
            <label><fmt:message key="field.username"/><input value="${target.loginName}" disabled></label>
            <label><fmt:message key="field.email"/><input name="mailAddress" value="${target.mailAddress}" required></label>
            <label><fmt:message key="field.mobile"/><input name="mobile" value="${target.mobile}" required></label>
            <label><fmt:message key="field.role"/>
                <select name="roleCode">
                    <option value="learner" ${target.roleCode == 'learner' ? 'selected' : ''}>learner</option>
                    <option value="manager" ${target.roleCode == 'manager' ? 'selected' : ''}>manager</option>
                </select>
            </label>
            <button class="button"><fmt:message key="btn.save"/></button>
        </form>
    </main>
</div>
</body>
</html>
