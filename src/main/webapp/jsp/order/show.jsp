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
                            Order Name:
                        </div>
                        <div class="col-md-6 text-left">
                            ${orderCommand.name}
                        </div>
                    </div>
                    <div class="row">

                        <div class="col-md-6 text-right">
                            Order Date:
                        </div>
                        <div class="col-md-6 text-left">
                            <fmt:formatDate type="date" dateStyle="long"   value="${orderCommand.date}" />
                        </div>
                    </div>
                    <div class="row">

                        <div class="col-md-6 text-right">
                            Area:
                        </div>
                        <div class="col-md-6 text-left">
                            ${order.area}
                        </div>
                    </div>
                    <div class="row">

                        <div class="col-md-6 text-right">
                            Product:
                        </div>
                        <div class="col-md-6 text-left">
                            ${orderCommand.product}
                        </div>
                    </div>
                    <div class="row">

                        <div class="col-md-6 text-right">
                            Material Unit Cost:
                        </div>
                        <div class="col-md-6 text-left">

                            <fmt:setLocale value="en_US"/>
                            <fmt:formatNumber value="${order.costPerSquareFoot}" type="currency"/>

                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 text-right">
                            Material Total Cost:
                        </div>
                        <div class="col-md-6 text-left">
                            <fmt:setLocale value="en_US"/>
                            <fmt:formatNumber value="${order.materialCost}" type="currency"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 text-right">
                            Installation Unit Cost:
                        </div>
                        <div class="col-md-6 text-left">
                            <fmt:setLocale value="en_US"/>
                            <fmt:formatNumber value="${order.laborCostPerSquareFoot}" type="currency"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 text-right">
                            Installation Total Cost:
                        </div>
                        <div class="col-md-6 text-left">
                            <fmt:setLocale value="en_US"/>
                            <fmt:formatNumber value="${order.laborCost}" type="currency"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 text-right">
                            Total Before Tax:
                        </div>
                        <div class="col-md-6 text-left">
                            <fmt:setLocale value="en_US"/>
                            <fmt:formatNumber value="${order.total - order.tax}" type="currency"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 text-right">
                            Sales Tax Rate:
                        </div>
                        <div class="col-md-6 text-left">
                            ${order.taxRate}%
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 text-right">
                            Total Sales Tax:
                        </div>
                        <div class="col-md-6 text-left">
                            <fmt:setLocale value="en_US"/>
                            <fmt:formatNumber value="${order.tax}" type="currency"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 text-right">
                            Total Invoice:
                        </div>
                        <div class="col-md-6 text-left">
                            <fmt:setLocale value="en_US"/>
                            <fmt:formatNumber value="${order.total}" type="currency"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="../partials/commonScript.jspf" %>
    </body>
</html>
