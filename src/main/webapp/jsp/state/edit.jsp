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
                <div class="col-md-6">
                    <%@ include file="_list.jspf" %>
                </div>
                <div class="col-md-6 text-center">
                    <form:form method="POST" commandName="stateCommand" action="${pageContext.request.contextPath}/state/update" class="form-horizontal">
                        <c:if test="${stateError}" >
                            <div class="has-error">
                            </c:if>
                            <div class="form-group">
                                <div class="col-sm-3"></div>
                                <div class="col-sm-7 text-center">
                                    <strong><form:errors path="stateName" /></strong>
                                </div>
                            </div>

                            <div class="form-group">
                                <form:label path="stateName" for="stateName" class="col-sm-3 control-label" >State:</form:label>

                                    <div class="col-sm-7">
                                    <form:input path="stateName" style="text-align: center" class="form-control" type="text" name="stateName" id="stateName" placeholder="State Name" />
                                </div>
                            </div>
                            <c:if test="${stateError}" >
                            </div>
                        </c:if>

                        <c:if test="${taxError}" >
                            <div class="has-error">
                            </c:if>

                            <div class="form-group">
                                <div class="col-sm-3"></div>
                                <div class="col-sm-7 text-center">
                                    <strong><form:errors path="stateTax" /></strong>
                                </div>
                            </div>
                            <div class="form-group">
                                <form:label path="stateTax" for="stateTax" class="col-sm-3 control-label" >State Sales Tax:</form:label>
                                    <div class="col-sm-7">
                                    <form:input path="stateTax" style="text-align: center" class="form-control" type="text" name="stateTax" id="stateTax" placeholder="State Sales Tax" />

                                </div>
                            </div>

                            <c:if test="${taxError}" >
                            </div>
                        </c:if>
                        <div class="form-group">
                            <div class="col-sm-3"></div>
                            <div class="col-sm-7">
                                <input value="Update" type="submit" class="btn btn-default" />
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
    <%@ include file="../partials/commonScript.jspf" %>                
</body>
</html>
