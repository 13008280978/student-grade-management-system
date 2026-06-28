<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${empty sessionScope.pageLocale ? pageContext.request.locale : sessionScope.pageLocale}"/>
<fmt:setBundle basename="grade_text"/>
<!doctype html>
<html>
<head><title><fmt:message key="menu.rubric"/></title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grade.css"></head>
<body>
<div class="layout">
    <jsp:include page="/WEB-INF/views/frame-nav.jsp"/>
    <main class="workspace">
        <div class="bar"><h1><fmt:message key="menu.rubric"/></h1></div>
        <section class="box">
            <table>
                <tr><th>课程目标</th><th>评价环节</th><th>证据材料</th><th>达成口径</th></tr>
                <tr><td>掌握 JSP/Servlet 请求处理</td><td>课堂测验、项目登录模块</td><td>验证码、会话、权限控制代码</td><td>相关题目和功能均达到 70% 以上</td></tr>
                <tr><td>掌握 JDBC 数据访问</td><td>数据库实验、成绩录入模块</td><td>PreparedStatement、事务、连接池配置</td><td>可完成增删改查和统计查询</td></tr>
                <tr><td>具备成绩数据分析能力</td><td>综合项目、课程报告</td><td>分段统计、绩点汇总、学业预警</td><td>能根据分数和学分生成教务结论</td></tr>
            </table>
        </section>
    </main>
</div>
</body>
</html>
