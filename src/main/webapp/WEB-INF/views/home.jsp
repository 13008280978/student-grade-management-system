<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<fmt:message key="home.average" var="averageLabel"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="menu.home"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<div class="layout">
    <jsp:include page="frame-nav.jsp"/>
    <main class="workspace">
        <div class="bar"><h1><fmt:message key="menu.home"/></h1><span>${sessionScope.signedAccount.loginName}</span></div>
        <section class="metrics">
            <div class="metric"><fmt:message key="home.accounts"/><strong>${accountCount}</strong></div>
            <div class="metric"><fmt:message key="home.students"/><strong>${studentCount}</strong></div>
            <div class="metric"><fmt:message key="home.courses"/><strong>${courseCount}</strong></div>
            <div class="metric"><fmt:message key="home.scores"/><strong>${scoreCount}</strong></div>
            <div class="metric"><fmt:message key="home.average"/><strong>${avgScore}</strong></div>
        </section>
        <section class="box">
            <canvas id="scoreChart" height="92"></canvas>
        </section>
    </main>
</div>
<script>
const labels = [<c:forEach items="${courseAverages}" var="item" varStatus="s">"${item.courseName}"${s.last ? '' : ','}</c:forEach>];
const values = [<c:forEach items="${courseAverages}" var="item" varStatus="s">${item.averageScore}${s.last ? '' : ','}</c:forEach>];
new Chart(document.getElementById("scoreChart"), {
    type: "line",
    data: {labels, datasets: [{label: "${averageLabel}", data: values, borderColor: "#0f766e", backgroundColor: "rgba(15,118,110,.16)", fill: true, tension: .3}]}
});
</script>
</body>
</html>
