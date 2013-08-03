<c:if test="${errorMessage != null}">
<div class="alert alert-error">
<c:out value="${errorMessage}"/>
<c:if test="${exception != null}">
    <button type="button" class="btn btn-mini btn-info pull-right" data-toggle="button" onclick="javascript:$('#excpetionTrace').slideToggle();"><i class="icon-eye-close icon-white"></i> Full trace</button>
<pre id="excpetionTrace" style="display: none;">
<%
    Exception exception = (Exception) request.getAttribute("exception");
    if (exception != null)
        exception.printStackTrace(new java.io.PrintWriter(out));
%>
</pre>
</c:if>
</div>
</c:if>