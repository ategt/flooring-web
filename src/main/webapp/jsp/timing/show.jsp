<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Flooring Master</title>
        <%@ include file="../partials/commonHead.jspf" %>
    </head>
    <body>
        <div class="container">
            <h1>Flooring Master</h1>
            <hr/>
            <%@ include file="../partials/banner.jspf" %>

            <div class="row">
                <div class="col-md-6 col-md-offset-3 text-center">

                    <div class="row">
                        <div class="col-md-6 text-right">
                            Timing ID:
                        </div>
                        <div class="col-md-6 text-left">
                            ${timing.id}
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 text-right">
                            Start Time:
                        </div>
                        <div class="col-md-6 text-left">
                            <fmt:formatDate type="date" dateStyle="long" value="${timing.startTime}" />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 text-right">
                            Stop Time:
                        </div>
                        <div class="col-md-6 text-left">
                            <fmt:formatDate type="date" dateStyle="long" value="${timing.stopTime}" />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 text-right">
                            Difference Of Time:
                        </div>
                        <div class="col-md-6 text-left">
                            ${timing.differenceTime} ms
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 text-right">
                            Modifiers:
                        </div>
                        <div class="col-md-6 text-left">
                            ${timing.modifiers}
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 text-right">
                            Invoking Class Name:
                        </div>
                        <div class="col-md-6 text-left">
                            ${timing.invokingClassName}
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 text-right">
                            Invoking Method Name:
                        </div>
                        <div class="col-md-6 text-left">
                            ${timing.invokingMethodName}
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="../partials/commonScript.jspf" %>
    </body>
</html>