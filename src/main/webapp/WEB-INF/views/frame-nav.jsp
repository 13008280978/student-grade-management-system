<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<aside class="rail">
    <h2><fmt:message key="app.title"/></h2>
    <a href="${pageContext.request.contextPath}/home"><fmt:message key="menu.home"/></a>
    <c:if test="${sessionScope.signedAccount.roleCode == 'manager'}">
        <a href="${pageContext.request.contextPath}/accounts"><fmt:message key="menu.accounts"/></a>
    </c:if>
    <a href="${pageContext.request.contextPath}/students"><fmt:message key="menu.students"/></a>
    <a href="${pageContext.request.contextPath}/courses"><fmt:message key="menu.courses"/></a>
    <a href="${pageContext.request.contextPath}/scores"><fmt:message key="menu.scores"/></a>
    <a href="${pageContext.request.contextPath}/transcript"><fmt:message key="menu.transcript"/></a>
    <a href="${pageContext.request.contextPath}/alerts"><fmt:message key="menu.alerts"/></a>
    <a href="${pageContext.request.contextPath}/credits"><fmt:message key="menu.credits"/></a>
    <c:if test="${sessionScope.signedAccount.roleCode == 'manager'}">
        <a href="${pageContext.request.contextPath}/analysis"><fmt:message key="menu.analysis"/></a>
        <a href="${pageContext.request.contextPath}/scholarship"><fmt:message key="menu.scholarship"/></a>
        <a href="${pageContext.request.contextPath}/program"><fmt:message key="menu.program"/></a>
        <a href="${pageContext.request.contextPath}/rubric.jsp"><fmt:message key="menu.rubric"/></a>
    </c:if>
    <a href="${pageContext.request.contextPath}/notice.jsp"><fmt:message key="menu.notice"/></a>
    <a href="${pageContext.request.contextPath}/profile.jsp"><fmt:message key="menu.profile"/></a>
    <a href="${pageContext.request.contextPath}/help.jsp"><fmt:message key="menu.help"/></a>
    <a href="${pageContext.request.contextPath}/language?to=zh"><fmt:message key="menu.zh"/></a>
    <a href="${pageContext.request.contextPath}/language?to=en"><fmt:message key="menu.en"/></a>
    <a href="${pageContext.request.contextPath}/signout"><fmt:message key="menu.logout"/></a>
</aside>
