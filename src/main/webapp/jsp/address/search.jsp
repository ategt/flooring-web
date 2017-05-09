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
                    <li role="presentation"><a href="${pageContext.request.contextPath}/search">Search</a></li>


                </ul>    
            </div>

            <div class="row">
                <div class="col-md-6">

                    <table class="table table-hover">
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th><i class="glyphicon glyphicon-edit"></i> Edit</th>
                        <th><i class="glyphicon glyphicon-remove"></i> Delete</th>
                            <c:forEach items="${addresses}" var="address">
                            <tr>
                                <td><a href="addressbook/show/${address.id}">${address.firstName}</a></td>
                                <td><a href="addressbook/show/${address.id}">${address.lastName}</a></td>
                                <td><a href="addressbook/edit/${address.id}">Edit</a></td>
                                <td><a href="addressbook/delete/${address.id}">Delete</a></td>

                            </tr>
                        </c:forEach>



                    </table>




                </div>
                
                <div class="col-md-6">
                    <form method="POST" action="./search" class="form-horizontal">
                        <div class="form-group">
                            <div class="col-md-4">
                                <!--                            <label for="title" class="col-sm-2 control-label" >Title:</label>
                                                            <div class="col-sm-10">
                                                                <input class="form-control" type="text" name="title" id="title" placeholder="Title" />
                                                            </div>-->

                                <select name="searchBy">
                                    <option value="searchByFirstName" >Search By First Name</option>
                                    <option value="searchByLastName" >Search By Last Name</option>
                                    <option value="searchByCity" />Search By City</option>
                                    <option value="searchByState" />Search By State</option>
                                    <option value="searchByZip" />Search By Zip Code</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <input class="form-control" type="text" name="searchText" id="searchText" placeholder="Type Search Criteria Here" />
                            </div>
                            <div class="form-group">
                                <!--                            <div class="col-md-6"></div>-->
                                <div class="col-md-10 pull-right">
                                    <input type="submit" class="btn btn-default" />
                                </div>
                            </div>
                            <!--                        
                                                    <div class="form-group">
                            
                                                        <label for="releaseDate" class="col-sm-2 control-label" >Release Date:</label>
                                                        <div class="col-sm-10">
                                                            <input class="form-control" type="date" name="releaseDate" id="releaseDate" placeholder="Release Date" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="rating" class="col-sm-2 control-label" >Rating:</label>
                                                        <div class="col-sm-10">
                                                            <input class="form-control" type="text" name="rating" id="rating" placeholder="Rating" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="directorsName" class="col-sm-2 control-label" >Director:</label>
                                                        <div class="col-sm-10">
                                                            <input class="form-control" type="text" name="directorsName" id="directorsName" placeholder="Director's Name" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="studio" class="col-sm-2 control-label" >Studio:</label>
                                                        <div class="col-sm-10">
                                                            <input class="form-control" type="text" name="studio" id="studio" placeholder="Studio's Name" />
                                                        </div>
                                                    </div>-->
                            <!--                        <div class="form-group">
                                                        <label for="note" class="col-sm-2 control-label" >Note:</label>
                                                        <div class="col-sm-10">
                                                            
                                                            Notes Is Not Supported Yet.
                                                            
                                                            <textarea name="note" id="note" class="form-control" rows="3"></textarea>
                                                            <input class="form-control" type="text" name="studio" id="studio" placeholder="Studio's Name" />
                                                        </div>
                                                    </div>-->
                            <!--                        <div class="form-group">
                                                        <div class="col-sm-2"></div>
                                                        <div class="col-sm-10">
                                                            <input type="submit" class="btn btn-default" />
                                                                                            <input type="submit" class="btn btn-danger" />
                                                                                            <input type="submit" class="btn btn-warning" />
                                                        </div>
                                                    </div>-->

                        </div>
                    </form>
                </div>






            </div>
        </div>
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="${pageContext.request.contextPath}/js/jquery-1.11.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

    </body>
</html>

