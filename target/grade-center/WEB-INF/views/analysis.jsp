<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<fmt:message key="analysis.count" var="countLabel"/>
<!doctype html>
<html>
<head>
    <title><fmt:message key="analysis.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<div class="layout">
    <jsp:include page="frame-nav.jsp"/>
    <main class="workspace">
        <div class="bar"><h1><fmt:message key="analysis.title"/></h1></div>
        <form class="box grid-form" method="get">
            <label><fmt:message key="field.term"/><input name="term" value="${param.term}" placeholder="2026-春"></label>
            <button class="button"><fmt:message key="btn.search"/></button>
        </form>
        <section class="box">
            <canvas id="bandChart" height="88"></canvas>
        </section>
        <table>
            <tr><th><fmt:message key="field.studentNo"/></th><th><fmt:message key="field.name"/></th><th><fmt:message key="home.average"/></th></tr>
            <c:forEach items="${ranks}" var="r">
                <tr><td>${r.studentNo}</td><td>${r.fullName}</td><td>${r.averageScore}</td></tr>
            </c:forEach>
        </table>
    </main>
</div>
<script>
const bandNames = [<c:forEach items="${bands}" var="b" varStatus="s">"${b.bandName}"${s.last ? '' : ','}</c:forEach>];
const bandCounts = [<c:forEach items="${bands}" var="b" varStatus="s">${b.amount}${s.last ? '' : ','}</c:forEach>];
new Chart(document.getElementById("bandChart"), {
    type: "bar",
    data: {labels: bandNames, datasets: [{label: "${countLabel}", data: bandCounts, backgroundColor: ["#0f766e", "#2563eb", "#ca8a04", "#ea580c", "#b91c1c"]}]}
});
</script>
</body>
</html>
