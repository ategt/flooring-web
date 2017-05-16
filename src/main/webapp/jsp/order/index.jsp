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
        <link href="${pageContext.request.contextPath}/css/jquery-ui.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/jquery-ui.structure.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/jquery-ui.theme.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container">
            <h1>Flooring Master</h1>
            <hr/>

            <%@ include file="../partials/banner.jspf" %>

            <div class="row">                
                <div class="col-md-6 col-md-push-6">
                    <%@ include file="_createOrderForm.jspf" %>
                </div>
                <div class="col-md-6 col-md-pull-6">
                    <%@ include file="_listOrders.jspf" %>
                </div>
            </div>
        </div>

        <%@ include file="_showOrderModal.jspf" %>
        <%@ include file="_editOrderModal.jspf" %>

        <!-- Placed at the end of the document so the pages load faster -->
        <%@ include file="../partials/commonScript.jspf" %>

        <script src="${pageContext.request.contextPath}/jquery-ui-1.12.1/jquery-ui.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/flooringapp.js"></script>
        <script>
            $(function () {
                $("#jQueryDatePicker").datepicker();
                $("#jQueryDatePicker").datepicker('setDate', new Date());
            });
        </script>

    </body>
</html>

