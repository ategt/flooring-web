<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Flooring Master</title>
        <!-- Bootstrap core CSS -->
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">

        <!-- Custom styles for this template -->
        <link href="${pageContext.request.contextPath}/css/starter-template.css" rel="stylesheet">

        <!-- SWC Icon -->
        <link rel="shortcut icon" href="${pageContext.request.contextPath}/img/icon.png">

    </head>
    <body>
        <div class="container">
            <h1>Flooring Master</h1>
            
            <hr/>
            
            <%@ include file="adminBanner.jspf" %>
            
            <div class="row">
                <div class="col-md-6">

                    <table class="table table-hover">
                        <th>Product Name</th>
                        <th>Product Material Cost</th>
                        <th>Product Installation Cost</th>
                        <th><i class="glyphicon glyphicon-edit"></i> Edit</th>
                        <th><i class="glyphicon glyphicon-remove"></i> Delete</th>
                            <c:forEach items="${productCommands}" var="product">
                            <tr>
                                <td><a href="${pageContext.request.contextPath}/adminProductPanel/edit/${product.productName}">${product.productName}</a></td>
                                <td><a href="${pageContext.request.contextPath}/adminProductPanel/edit/${product.productName}">${product.productCost}</a></td>
                                <td><a href="${pageContext.request.contextPath}/adminProductPanel/edit/${product.productName}">${product.laborCost}</a></td>
                                <td><a href="${pageContext.request.contextPath}/adminProductPanel/edit/${product.productName}">Edit</a></td>
                                <td><a href="${pageContext.request.contextPath}/adminProductPanel/delete/${product.productName}">Delete</a></td>
                            </tr>
                        </c:forEach>

                    </table>

                </div>


                <div class="col-md-6">

                    <table class="table table-hover">
                        <th>State Name</th>
                        <th>Sales Tax</th>
                        <th><i class="glyphicon glyphicon-edit"></i> Edit</th>
                        <th><i class="glyphicon glyphicon-remove"></i> Delete</th>
                            <c:forEach items="${states}" var="state">
                            <tr>
                                <td><a href="${pageContext.request.contextPath}/adminStatePanel/edit/${state.stateAbbreviation}">${state.stateName}</a></td>
                                <td><a href="${pageContext.request.contextPath}/adminStatePanel/edit/${state.stateAbbreviation}">${state.stateTax}</a></td>
                                <td><a href="${pageContext.request.contextPath}/adminStatePanel/edit/${state.stateAbbreviation}">Edit</a></td>
                                <td><a href="${pageContext.request.contextPath}/adminStatePanel/delete/${state.stateAbbreviation}">Delete</a></td>
                            </tr>
                        </c:forEach>

                    </table>

                </div>


            </div>
        </div>
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="${pageContext.request.contextPath}/js/jquery-1.11.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

    </body>
</html>
