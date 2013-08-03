<%--
  Created by IntelliJ IDEA.
  User: rosty
  Date: 7/21/13
  Time: 9:07 PM
  To change this template use File | Settings | File Templates.
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