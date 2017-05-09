<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Address Book</title>
        <!-- Bootstrap core CSS -->
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">

        <!-- Custom styles for this template -->
        <link href="${pageContext.request.contextPath}/css/starter-template.css" rel="stylesheet">

        <!-- SWC Icon -->
        <link rel="shortcut icon" href="${pageContext.request.contextPath}/img/icon.png">

    </head>
    <body>
        <div class="container">
            <h1>Address Book</h1>
            <hr/>
            <div class="navbar">
                <ul class="nav nav-tabs">
                    <li role="presentation"><a href="${pageContext.request.contextPath}">Home</a></li>


                </ul>    
            </div>

            <div class="row">

                <div class="col-md-6 text-center">
                    <h2>
                        <c:choose>
                            <c:when test="${lastNameFirst}">
                                ${address.lastName}, ${address.firstName}<br />
                            </c:when>
                            <c:otherwise>
                                ${address.firstName} ${address.lastName}<br />
                            </c:otherwise>
                        </c:choose>
                        ${address.streetNumber} ${address.streetName}<br />
                        ${address.city}, ${address.state} ${address.zip}<br />
                    </h2>
                    <div class="col-sm-6">
                        <a href="${pageContext.request.contextPath}/addressbook/show/${address.id}/true">Last Name First</a>
                    </div>
                    <div class="col-sm-6">
                        <a href="${pageContext.request.contextPath}/addressbook/show/${address.id}/false">First Name First</a>
                    </div>
                    <div class="col-sm-12 text-center">
                        <a href="${pageContext.request.contextPath}/addressbook/edit/${address.id}">Edit</a>
                    </div>
                </div>
                <div class="col-md-6 text-center">
                    <table class="table table-hover">
                        <tr>
                            <td> 
                                First: 
                            </td>
                            <td>
                                ${address.firstName}
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Last: 
                            </td>
                            <td>
                                ${address.lastName}
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Address: 
                            </td>
                            <td>
                                ${address.streetNumber} ${address.streetName}
                            </td>
                        </tr>
                        <tr>
                            <td>
                                City:
                            </td>
                            <td>
                                ${address.city}
                            </td>
                        </tr>
                        <tr>
                            <td>
                                State: 
                            </td>
                            <td>
                                ${address.state}
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Zip Code: 
                            </td>
                            <td>
                                ${address.zip}
                            </td>
                        </tr>
                    </table>

                </div>
            </div>






        </div>
    </div>
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

</body>
</html>

