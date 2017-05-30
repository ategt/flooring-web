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
                <div class="col-sm-6 col-sm-offset-2 text-center">

                    <div class="row timing-row">
                        <div class="col-md-6 timing-label">
                            Timing ID:
                        </div>
                        <div id="timing-id" class="col-md-6 timing-value">
                            ${timing.id}
                        </div>
                    </div>
                    <div class="row timing-row">
                        <div class="col-md-6 timing-label">
                            Start Time:
                        </div>
                        <div class="col-md-6 timing-value">
                            ${timing.startTime} ms
                        </div>
                    </div>
                    <div class="row timing-row">
                        <div class="col-md-6 timing-label">
                            Stop Time:
                        </div>
                        <div class="col-md-6 timing-value">
                            ${timing.stopTime} ms
                        </div>
                    </div>
                    <div class="row timing-row">
                        <div class="col-md-6 timing-label">
                            Difference Of Time:
                        </div>
                        <div id="timing-difference" class="col-md-6 timing-value">
                            ${timing.differenceTime} ms
                        </div>
                    </div>
                    <div class="row timing-row">
                        <div class="col-md-6 timing-label">
                            Modifiers:
                        </div>
                        <div class="col-md-6 timing-value">
                            ${timing.modifiers}
                        </div>
                    </div>
                    <div class="row timing-row">
                        <div class="col-md-6 timing-label">
                            Invoking Class Name:
                        </div>
                        <div class="col-md-6 timing-value">
                            ${timing.invokingClassName}
                        </div>
                    </div>
                    <div class="row timing-row">
                        <div class="col-md-6 timing-label">
                            Invoking Method Name:
                        </div>
                        <div class="col-md-6 timing-value">
                            ${timing.invokingMethodName}
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="../partials/commonScript.jspf" %>
    </body>
</html>