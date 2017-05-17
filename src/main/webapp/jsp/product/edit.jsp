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
                    <form:form method="POST" commandName="productCommand" action="${pageContext.request.contextPath}/product/update" class="form-horizontal">
                        <c:if test="false" >
                            <div class="has-error">
                            </c:if>
                            <div class="form-group">
                                <div class="col-sm-3"></div>
                                <div class="col-sm-7 text-center">
                                    <strong><form:errors path="productName" /></strong>
                                </div>
                            </div>

                            <div class="form-group">
                                <form:label path="productName" for="productName" class="col-sm-3 control-label" >Product:</form:label>

                                    <div class="col-sm-7">
                                    <form:input path="productName" style="text-align: center" class="form-control" type="text" id="productName" placeholder="Product Name" />
                                </div>
                            </div>
                            <c:if test="false" >
                                </>
                            </c:if>

                            <c:if test="false" >
                                <div class="has-error">
                                </c:if>

                                <div class="form-group">
                                    <div class="col-sm-3"></div>
                                    <div class="col-sm-7 text-center">
                                        <strong><form:errors path="productCost" /></strong>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <form:label path="productCost" for="productCost" class="col-sm-3 control-label" >Material Cost:</form:label>
                                        <div class="col-sm-7">
                                        <form:input path="productCost" style="text-align: center" class="form-control" type="text" id="productCost" placeholder="State Sales Tax" />

                                    </div>
                                </div>

                                <c:if test="false" >
                                </div>
                            </c:if>

                            <c:if test="false" >
                                <div class="has-error">
                                </c:if>

                                <div class="form-group">
                                    <div class="col-sm-3"></div>
                                    <div class="col-sm-7 text-center">
                                        <strong><form:errors path="laborCost" /></strong>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <form:label path="laborCost" for="laborCost" class="col-sm-3 control-label" >Installation Cost:</form:label>
                                        <div class="col-sm-7">
                                        <form:input path="laborCost" style="text-align: center" class="form-control" type="text" id="laborCost" placeholder="Installation Cost" />
                                    </div>
                                </div>

                                <c:if test="$false" >
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
