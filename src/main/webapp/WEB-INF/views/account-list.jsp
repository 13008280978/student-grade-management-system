<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="account.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
    <script src="${pageContext.request.contextPath}/assets/js/grade-check.js"></script>
</head>
<body>
<div class="layout">
    <jsp:include page="frame-nav.jsp"/>
    <main class="workspace">
        <div class="bar"><h1><fmt:message key="account.title"/></h1></div>
        <form class="box grid-form" method="get">
            <label><fmt:message key="field.username"/><input name="loginName" value="${param.loginName}"></label>
            <label><fmt:message key="field.email"/><input name="mailAddress" value="${param.mailAddress}"></label>
            <label><fmt:message key="field.dateFrom"/><input type="date" name="joinedFrom" value="${param.joinedFrom}"></label>
            <label><fmt:message key="field.dateTo"/><input type="date" name="joinedTo" value="${param.joinedTo}"></label>
            <button class="button"><fmt:message key="btn.search"/></button>
        </form>
        <table>
            <tr><th><fmt:message key="field.id"/></th><th><fmt:message key="field.username"/></th><th><fmt:message key="field.email"/></th><th><fmt:message key="field.mobile"/></th><th><fmt:message key="field.role"/></th><th><fmt:message key="field.status"/></th><th><fmt:message key="field.action"/></th></tr>
            <c:forEach items="${accounts}" var="a">
                <tr>
                    <td>${a.accountId}</td><td>${a.loginName}</td><td>${a.mailAddress}</td><td>${a.mobile}</td><td>${a.roleCode}</td><td>${a.disabled == 0 ? '正常' : '已禁用'}</td>
                    <td>
                        <a class="button" href="${pageContext.request.contextPath}/accounts/edit?id=${a.accountId}"><fmt:message key="btn.edit"/></a>
                        <a class="button gray" href="${pageContext.request.contextPath}/accounts/password?id=${a.accountId}"><fmt:message key="btn.reset"/></a>
                        <form method="post" action="${pageContext.request.contextPath}/accounts/disable" style="display:inline" onsubmit="return ask('确认禁用？')"><input type="hidden" name="id" value="${a.accountId}"><button class="button red"><fmt:message key="btn.disable"/></button></form>
                        <form method="post" action="${pageContext.request.contextPath}/accounts/erase" style="display:inline" onsubmit="return ask('确认删除？')"><input type="hidden" name="id" value="${a.accountId}"><button class="button red"><fmt:message key="btn.delete"/></button></form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </main>
</div>
</body>
</html>
