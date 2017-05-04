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

                    <table id="address-table" class="table table-hover">
                        <th>ID</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th><i class="glyphicon glyphicon-edit"></i> Edit</th>
                        <th><i class="glyphicon glyphicon-remove"></i> Delete</th>

                       <c:forEach items="${addresses}" var="address">
                            <tr id="address-row-${address.id}" >

                                <td>${address.id}</td>
                                <td><a data-address-id="${address.id}" data-toggle="modal" data-target="#showDetailModal">${address.firstName}</a></td>
                                <td><a href="contact/show/${address.id}">${address.lastName}</a></td>
                                <td><a data-address-id="${address.id}" data-toggle="modal" data-target="#editDetailModal">Edit</a></td>
                                <td><a data-address-id="${address.id}" class="delete-link">Delete</a></td>

                            </tr>
                        </c:forEach>



                    </table>




                </div>
                <div class="col-md-6">
                    <form method="POST" class="form-horizontal" id="createAddressForm" >
                        <input type="hidden" id="objectId" value="0" />
                        <div class="form-group color-container" id="firstName-group">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-10 error-container" id="firstName-errors" ></div>
                            <label for="firstName" class="col-sm-2 control-label" >First:</label>
                            <div class="col-sm-10">
                                <input class="form-control create-address-form" type="text" name="firstName" id="firstName" placeholder="First Name" />
                            </div>
                        </div>
                        <div class="form-group color-container" id="lastName-group" >
                            <div class="col-sm-2"></div>
                            <div class="col-sm-10 error-container" id="lastName-errors" ></div>

                            <label for="lastName" class="col-sm-2 control-label" >Last:</label>
                            <div class="col-sm-10">
                                <input class="form-control create-address-form" type="text" name="lastName" id="lastName" placeholder="Last Name" />
                            </div>
                        </div>
                        <div class="form-group color-container" id="streetNumber-group" >
                            <div class="col-sm-2"></div>
                            <div class="col-sm-10 error-container" id="streetNumber-errors" ></div>

                            <label for="streetNumber" class="col-sm-2 control-label" >Street Number:</label>
                            <div class="col-sm-10">
                                <input class="form-control create-address-form" type="text" name="streetNumber" id="streetNumber" placeholder="Street Number" />
                            </div>
                        </div>
                        <div class="form-group color-container" id="streetName-group">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-10 error-container" id="streetName-errors" ></div>

                            <label for="streetName" class="col-sm-2 control-label" >Street Name:</label>
                            <div class="col-sm-10">
                                <input class="form-control create-address-form" type="text" name="streetName" id="streetName" placeholder="Street Name" />
                            </div>
                        </div>
                        <div class="form-group color-container" id="city-group">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-10 error-container" id="city-errors" ></div>

                            <label for="city" class="col-sm-2 control-label" >City:</label>
                            <div class="col-sm-10">
                                <input class="form-control create-address-form" type="text" name="city" id="city" placeholder="City" />
                            </div>
                        </div>
                        <div class="form-group color-container" id="state-group">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-10 error-container" id="state-errors" ></div>

                            <label for="state" class="col-sm-2 control-label" >State:</label>
                            <div class="col-sm-10">
                                <input class="form-control create-address-form" type="text" name="state" id="state" placeholder="State" />
                            </div>
                        </div>
                        <div class="form-group color-container" id="zip-group">
                            <div class="col-sm-2"></div>
                            <div class="col-sm-10 error-container" id="zip-errors" ></div>

                            <label for="zip" class="col-sm-2 control-label" >Zip:</label>
                            <div class="col-sm-10">
                                <input class="form-control create-address-form" type="text" name="zip" id="zip" placeholder="Zip" />
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-2" id="add-contact-validation-errors" ></div>
                            <div class="col-sm-10">
                                <input type="submit" id="create-submit"  class="btn btn-default" />
                            </div>
                        </div>

                </div>
                </form>
            </div>


        </div>
    </div>



    <div id="showDetailModal" class="modal fade" role="dialog">
          <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Address Details</h4>
              </div>
              <div class="modal-body">

                    <table class="table table-bordered" id="show-address-table">
                        <tr>
                            <th>First Name:</th>
                            <td id="address-first-name"></td>
                        </tr>

                        <tr>
                            <th>Last Name:</th>
                            <td id="address-last-name"></td>
                        </tr>

                        <tr>
                            <th>Street Name:</th>
                            <td id="address-street-name"></td>
                        </tr>

                        <tr>
                            <th>Street Number:</th>
                            <td id="address-street-number"></td>
                        </tr>

                        <tr>
                            <th>City:</th>
                            <td id="address-city"></td>
                        </tr>

                        <tr>
                            <th>State:</th>
                            <td id="address-state"></td>
                        </tr>
                        <tr>
                            <th>Zip:</th>
                            <td id="address-zipcode"></td>
                        </tr>
                        
                    </table>

              </div>
              <div class="modal-footer">
                    <!--<button type=\"button\" data-address-id=\"" + data.id + "\" class=\"edit-from-detail-button btn btn-default\" data-dismiss=\"modal\">Edit</button>-->

                <!--<button type="button" class="edit-from-detail-button btn btn-default" data-dismiss="modal">Edit</button>-->
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>

              </div>
            </div>

          </div>
        </div>


    <div id="editDetailModal" class="modal fade" role="dialog">
          <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Edit Address Details</h4>
              </div>
              <div class="modal-body">

                    <table class="table table-bordered">

                        <tr>
                            <th>ID:</th>
                            <td>
                                <input type="text" id="edit-id" />
                            </td>
                        </tr>

                        <tr>
                            <th>First Name:</th>
                            <td>
                                <input type="text" id="edit-address-first-name" />
                            </td>
                        </tr>

                        <tr>
                            <th>Last Name:</th>
                            <td>
                                <input type="text" id="edit-address-last-name" />
                            </td>
                        </tr>

                        <tr>
                            <th>Street Name:</th>
                            <td>
                                <input type="text" id="edit-address-street-name" />
                            </td>
                        </tr>

                        <tr>
                            <th>Street Number:</th>
                            <td>
                                <input type="text" id="edit-address-street-number" />
                            </td>
                        </tr>

                        <tr>
                            <th>City:</th>
                            <td><input type="text" id="edit-address-city" /></td>
                        </tr>

                        <tr>
                            <th>State:</th>
                            <td><input type="text" id="edit-address-state" /></td>
                        </tr>

                        <tr>
                            <th>Zipcode:</th>
                            <td><input type="text" id="edit-address-zipcode" /></td>
                        </tr>

                        <!--                      <tr>
                                                  <th>Last Contacted:</th>
                                                  <td> <input type="text" id="edit-address-last-contacted" /></td>
                                              </tr>-->




                    </table>

              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-danger" id="edit-address-button">Save</button>
              </div>
            </div>

          </div>
        </div>






    <script>
        var contextRoot = "${pageContext.request.contextPath}";
    </script>

    <!-- Placed at the end of the document so the pages load faster -->
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>

</body>
</html>

