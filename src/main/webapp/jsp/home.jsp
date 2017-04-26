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


        <!--<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css" />-->
        <!--<script src="http://code.jquery.com/jquery-1.10.2.js"></script>-->

        <link rel="stylesheet" href="http://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <script src="http://code.jquery.com/jquery-1.10.2.js"></script>
        <script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>

    </head>
    <body>
        <div class="container">
            <h1>Flooring Master</h1>
            <hr/>
            <!--            <div class="navbar">
                            <ul class="nav nav-tabs">
                                <li role="presentation"><a href="${pageContext.request.contextPath}">Home</a></li>
                                <li role="presentation"><a href="${pageContext.request.contextPath}/search">Search</a></li>
            
                            </ul>    
                        </div>-->



            <%@ include file="banner.jspf" %>


            <div class="row">
                <div class="col-md-6">

                    <table id="order-table" class="table table-hover">
                        <th>Order Number</th>
                        <th>Order Name</th>
                        <th><i class="glyphicon glyphicon-edit"></i> Edit</th>
                        <th><i class="glyphicon glyphicon-remove"></i> Delete</th>
                            <c:forEach items="${orders}" var="order">
                            <!--                            <tr>
                                                            <td><a href="${pageContext.request.contextPath}/FlooringMaster/show/${order.id}">${order.id}</a></td>
                                                            <td><a href="${pageContext.request.contextPath}/FlooringMaster/show/${order.id}">${order.name}</a></td>
                                                            <td><a href="${pageContext.request.contextPath}/FlooringMaster/edit/${order.id}">Edit</a></td>
                                                            <td><a href="${pageContext.request.contextPath}/FlooringMaster/delete/${order.id}">Delete</a></td>
                            
                                                        </tr>-->



                            <tr id="order-row-${order.id}" >

<!--                                <td>${order.id}</td>-->
                                <td><a data-order-id="${order.id}" data-toggle="modal" data-target="#showDetailModal">${order.id}</a></td>
                                <td><a href="contact/show/${order.id}">${order.name}</a></td>
                                <td><a data-order-id="${order.id}" data-toggle="modal" data-target="#editDetailModal">Edit</a></td>
                                <td><a data-order-id="${order.id}" class="delete-link">Delete</a></td>

                            </tr>


                        </c:forEach>



                    </table>




                </div>
                <div class="col-md-6">
                    <form method="POST" class="form-horizontal">
                        <!--<input type=hidden path="id" />-->


                        <div class="form-group">
                            <div class="col-sm-3"></div>
                            <div class="col-sm-9 text-center">
                                <strong><span class="name" /></strong>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="name" class="col-sm-3 control-label" >Order Name:</label>
                            <div class="col-sm-9">
                                <input name="name" class="form-control" style="text-align: center" type="text" id="name" placeholder="Order Name" />
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-3"></div>
                            <div class="col-sm-9 text-center">
                                <strong><span class="state" /></strong>
                            </div>
                        </div>
                        <div class="form-group">

                            <label for="state" class="col-sm-3 control-label" >State:</label>
                            <div class="col-sm-9">
                                <!--<input name="state" class="form-control" style="text-align: center" type="text" id="state" placeholder="State" />-->

                                <select id="state-selector" style="text-align: center" class="form-control" name="state" >
                                    <c:forEach items="${stateCommands}" var="stateCommand">
                                        <option value="${stateCommand.stateAbbreviation}"  >${stateCommand.stateName} - ${stateCommand.stateTax}%</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>



                        <div class="form-group">
                            <div class="col-sm-3"></div>
                            <div class="col-sm-9 text-center">
                                <strong><span class="product" /></strong>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="product" class="col-sm-3 control-label" >Product:</label>
                            <div class="col-sm-9">
                                <!--<input name="product" class="form-control" style="text-align: center" type="text" id="product" placeholder="Product" />-->

                                <select name="product" id="product-selector" class="form-control" name="state-drop-down" >
                                    <c:forEach items="${productCommands}" var="productCommand">
                                        <option value="${productCommand.productName}" style="text-align: center">${productCommand.productName}</option>
                                    </c:forEach>
                                </select>


                            </div>
                        </div>


                        
                        
                        <div class="form-group">
                            <div class="col-sm-3"></div>
                            <div class="col-sm-9 text-center">
                                <strong><span class="date" /></strong>
                            </div>
                        </div>
                        <!--                        <div class="form-group">
                                                    <label for="date" class="col-sm-3 control-label" >Date:</label>
                                                    <div class="col-sm-9">
                                                        <input name="date" class="form-control" style="text-align: center" pattern="MM/dd/yyyy" type="date" id="datepicker" />
                                                    </div>
                                                </div>-->

                        <div class="form-group">
                            <label for="dateb" class="col-sm-3 control-label" >Date:</label>
                            <div class="col-sm-9">
                                <input name="dateb" class="form-control" style="text-align: center" pattern="MM/dd/yyyy" type="text" id="jQueryDatePicker"  />
                            </div>
                        </div>




                        <div class="form-group">
                            <div class="col-sm-3"></div>
                            <div class="col-sm-9 text-center">
                                <strong><span class="area" /></strong>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="area" class="col-sm-3 control-label" >Area:</label>
                            <div class="col-sm-9">
                                <input name="area" class="form-control" style="text-align: center" type="text" id="area" placeholder="Area" />
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-3"></div>
                            <div class="col-sm-9 text-center">
                              
                                <div id="add-contact-validation-errors"></div>
                                
                                
                                <input value="Create" id="create-submit" type="submit" class="btn btn-default" />
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
                <h4 class="modal-title">Order Details</h4>
              </div>
              <div class="modal-body">

                    <table class="table table-bordered table-striped" id="show-order-table">
                        <tr>
                            <th>Order Name:</th>
                            <td id="order-name"></td>
                        </tr>

                        <tr>
                            <th>State:</th>
                            <td id="order-state"></td>
                        </tr>

                        <tr>
                            <th>Product:</th>
                            <td id="order-product"></td>
                        </tr>

                        <tr>
                            <th>Date:</th>
                            <td id="order-date"></td>
                        </tr>

                        <tr>
                            <th>Area:</th>
                            <td id="order-area"></td>
                        </tr>





                        <tr>

                            <th>
                                Order Date:
                            </th>
                            <td id="order-date-f">

                            </td>
                        </tr>


                        <tr>

                            <th>
                                Material Unit Cost:
                            </th>
                            <td id="order-material-unit-cost">


                            </td>
                        </tr>
                        <tr>


                            <th>
                                Material Total Cost:
                            </th>
                            <td id="order-material-cost">


                            </td>
                        </tr>
                        <tr>


                            <th>
                                Installation Unit Cost:
                            </th>
                            <td id="order-labor-unit-cost">

                            </td>
                        </tr>
                        <tr>

                            <th>
                                Installation Total Cost:
                            </th>
                            <td id="order-labor-total-cost">

                            </td>
                        </tr>
                        <tr>

                            <th>
                                Total Before Tax:
                            </th>
                            <td id="order-subtotal">

                            </td>
                        </tr>
                        <tr>

                            <th>
                                Sales Tax Rate:
                            </th>
                            <td id="order-tax-rate">

                            </td>
                        </tr>
                        <tr>


                            <th>
                                Total Sales Tax:
                            </th>
                            <td id="order-total-tax">

                            </td>
                        </tr>
                        <tr>


                            <th>
                                Total Invoice:
                            </th>
                            <td id="order-total-invoice">

                            </td>
                        </tr>



                </div>






















                <!--                      <tr>
                                          <th>State:</th>
                                          <td id="order-state"></td>
                                      </tr>-->
                <!--                      <tr>
                                          <th>Zip:</th>
                                          <td id="order-zipcode"></td>
                                      </tr>-->

                <!--                      <tr>
                                          <th>Last Contacted:</th>
                                          <td id="order-notes"></td>
                                      </tr>-->




                </table>

              </div>
              <div class="modal-footer">
                <!--<button type=\"button\" data-order-id=\"" + data.id + "\" class=\"edit-from-detail-button btn btn-default\" data-dismiss=\"modal\">Edit</button>-->

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
                <h4 class="modal-title">Edit Order Details</h4>
              </div>
              <div class="modal-body">

                <table class="table table-bordered">
                            <input type="hidden" id="edit-id" class="form-control" />

                    <tr>
                        <th>ID:</th>
                        <td id="edit-display-id">
                            
                        </td>
                    </tr>

                    <tr>
                        <th>Order Name:</th>
                        <td>
                            <input type="text" id="edit-order-name" class="form-control" />
                        </td>
                    </tr>

                    <tr>
                        <th>State:</th>
                        <td>
                            <!--<input type="text" id="edit-order-state" />-->
                            
                            
                                <select id="edit-order-state" class="form-control" name="state" >
                                    <c:forEach items="${stateCommands}" var="stateCommand">
                                        <option value="${stateCommand.stateAbbreviation}"  >${stateCommand.stateName} - ${stateCommand.stateTax}%</option>
                                    </c:forEach>
                                </select>
                            
                        </td>
                    </tr>
                    
                    <tr>
                        <th>Product:</th>
                        <td>
                            
                            
                            
                            
                            
                            <!--<input type="text" id="edit-order-product" />-->
                            
                            
                            
                            
                            
                            
                    
                                <select name="product" id="edit-order-product" class="form-control" >
                                    <c:forEach items="${productCommands}" var="productCommand">
                                        <option value="${productCommand.productName}" >${productCommand.productName}</option>
                                    </c:forEach>
                                </select>
                            
                            
                            
                            
                            
                            
                            
                            
                            
                            
                        </td>
                    </tr>

                    <tr>
                        <th>Date:</th>
                        <td>
                            <!--<input type="date" pattern="MM/dd/yyyy" id="edit-order-date" class="order-date-class" />-->
                            <!--pattern="yyyy-MM-dd"-->
                            <input pattern="MM/dd/yyyy" type="text" class="form-control" id="edit-order-date"  />
                        </td>
                    </tr>

                    <tr>
                        <th>Area:</th>
                        <td><input type="text" id="edit-order-area" class="form-control" /></td>
                    </tr>
                    <!--                      
                                          <tr>
                                              <th>State:</th>
                                              <td><input type="text" id="edit-order-state" /></td>
                                          </tr>
                                          
                                          <tr>
                                              <th>Zipcode:</th>
                                              <td><input type="text" id="edit-order-zipcode" /></td>
                                          </tr>-->

                    <!--                      <tr>
                                              <th>Last Contacted:</th>
                                              <td> <input type="text" id="edit-order-last-contacted" /></td>
                                          </tr>-->




                </table>

              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-danger" id="edit-order-button">Save</button>
              </div>
            </div>

          </div>
        </div>






<script>
    $(function () {
        $("#jQueryDatePicker").datepicker();
        $("#jQueryDatePicker").datepicker('setDate', new Date());
    });
</script>
<script>
        var contextRoot = "${pageContext.request.contextPath}";

</script>
<!-- Placed at the end of the document so the pages load faster -->
<script src="${pageContext.request.contextPath}/js/jquery-1.11.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/app.js"></script>
<script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>

</body>
</html>

