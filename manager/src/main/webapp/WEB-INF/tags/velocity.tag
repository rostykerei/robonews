<%@tag description="Extended input tag to allow for sophisticated errors" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@attribute name="path" required="true" type="java.lang.String"%>
<%@attribute name="cssClass" required="false" type="java.lang.String"%>
<%@attribute name="label" required="false" type="java.lang.String"%>
<%@attribute name="required" required="false" type="java.lang.Boolean"%>

<c:if test="${empty label}">
    <c:set var="label" value="${fn:toUpperCase(fn:substring(path, 0, 1))}${fn:toLowerCase(fn:substring(path, 1,fn:length(path)))}" />
</c:if>
<spring:bind path="${path}">
    <div class="control-group ${status.error ? 'error' : '' }">
        <label class="control-label" for="${path}">${label}<c:if test="${required}"><span class="required">*</span></c:if></label>
        <div class="controls">
            <div id="${path}-slider" class="${empty cssClass ? 'input-xxlarge' : cssClass}"></div>

            <span id="${path}-slider-txt" style="color: #808080; font-size: 13px;"></span>

            <c:if test="${status.error}">
                <span class="help-inline">${status.errorMessage}</span>
            </c:if>

            <form:hidden id="${path}" path="${path}"/>
        </div>
    </div>
</spring:bind>

<script>
    $(function() {
        $( "#${path}-slider" ).slider({
            min: 0,
            max: 144,
            range: "min",
            value: logsliderRev($("#${path}").val()),
            step: 1,
            animate: true,
            slide: function( event, ui ) {
                $( "#${path}" ).val( logslider(ui.value) );
                $( "#${path}-slider-txt" ).text( velocityToTxt($("#${path}").val()) );
            }
        });

        $("#${path}-slider-txt").text( velocityToTxt($( "#${path}" ).val()) );
    });
</script>