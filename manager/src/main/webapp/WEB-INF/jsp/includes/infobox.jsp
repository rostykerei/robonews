<%--

    Robonews.io

    Copyright (c) 2013-2015 Rosty Kerei.
    All rights reserved.

--%>
<c:if test="${infoMessage != null}">
    <div class="alert alert-success">
        <c:out value="${infoMessage}"/>
    </div>
</c:if>