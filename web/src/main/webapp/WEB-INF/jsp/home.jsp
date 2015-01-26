<%--

    Robonews.io

    Copyright (c) 2013-2015 Rosty Kerei.
    All rights reserved.

--%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>home</title>
</head>
<body>
    <h2>home</h2>


    <h3>Channels</h3>
    <c:if  test="${!empty channels}">
        <table class="data">
            <tr>
                <th>Name</th>
            </tr>
            <c:forEach items="${channels}" var="channel">
                <tr>
                    <td>${channel.name}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</body>
</html>