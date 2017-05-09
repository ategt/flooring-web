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
                <form method="POST" action="./" class="form-horizontal">
                    <div class="col-md-6">
                        <div class="form-group">

                            <input type="hidden" name="id" id="id" value="${address.id}" />

                            <label for="firstName" class="col-sm-2 control-label" >First:</label>
                            <div class="col-sm-10">
                                <input class="form-control" type="text" value="${address.firstName}" name="firstName" id="firstName" placeholder="First Name" />
                            </div>

                        </div>
                        <div class="form-group">

                            <label for="lastName" class="col-sm-2 control-label" >Last:</label>
                            <div class="col-sm-10">
                                <input class="form-control" type="text" name="lastName" value="${address.lastName}" id="lastName" placeholder="Last Name" />
                            </div>
                        </div>          
                        <div class="form-group">
                            <label for="streetNumber" class="col-sm-2 control-label" >Street Number:</label>
                            <div class="col-sm-10">
                                <input class="form-control" type="text" name="streetNumber" value="${address.streetNumber} " id="streetNumber" placeholder="Street Number" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="streetName" class="col-sm-2 control-label" >Street Name:</label>
                            <div class="col-sm-10">
                                <input class="form-control" type="text" name="streetName" value="${address.streetName}" id="streetName" placeholder="Street Name" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="city" class="col-sm-2 control-label" >City:</label>
                            <div class="col-sm-10">
                                <input class="form-control" type="text" name="city" value="${address.city}" id="city" placeholder="City" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="state" class="col-sm-2 control-label" >State:</label>
                            <div class="col-sm-10">
                                <input class="form-control" type="text" name="state" value="${address.state}" id="state" placeholder="State" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="zip" class="col-sm-2 control-label" >Zip:</label>
                            <div class="col-sm-10">
                                <input class="form-control" type="text" name="zip" value="${address.zip}" id="zip" placeholder="Zip" />
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-2">

                            </div>
                            <div class="col-sm-10">
                                <input type="submit" value="Update" class="btn btn-default" />
                            </div>
                        </div>
                </form>
            </div>

        </div>






        <!-- Placed at the end of the document so the pages load faster -->
        <script src="${pageContext.request.contextPath}/js/jquery-1.11.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

    </body>
</html>

