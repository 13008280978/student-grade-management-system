<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head><title><fmt:message key="help.title"/></title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css"></head>
<body>
<div class="layout">
    <jsp:include page="/WEB-INF/views/frame-nav.jsp"/>
    <main class="workspace"><div class="bar"><h1><fmt:message key="help.title"/></h1></div><section class="box"><fmt:message key="help.body"/></section></main>
</div>
</body>
</html>
