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
                <div class="col-md-6 col-md-push-6 text-center">

                    <form method="POST" action="${pageContext.request.contextPath}/orders/search" class="form-horizontal">

                        <div class="form-group">

                            <div class="form-group">
                                <div class="col-md-2"></div>
                                <div class="col-md-6 text-center">
                                    <c:if test="${error}">
                                        <div class="text-center alert-warning">
                                            The System Could Not Understand That Input
                                        </div>
                                    </c:if>
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="col-md-2"></div>
                                <div class="col-md-6">
                                    <select name="searchBy" class="form-control">
                                        <option value="searchByEverything" >Search Orders By Everything</option>
                                        <option value="searchByOrderNumber" >Search By Order Number</option>
                                        <option value="searchByName" >Search By Order Name</option>
                                        <option value="searchByProduct" >Search By Product</option>
                                        <option value="searchByState" >Search By State</option>
                                        <option value="searchByDate" >Search By Date</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">

                                <div class="col-md-2"></div>
                                <div class="col-md-6 text-center">
                                    <c:if test="${dateError}">
                                        <div class="text-center alert-warning">
                                            The System Likes Dates in YYYY-DD-MM
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-2"></div>
                                <div class="col-md-6">
                                    <input class="form-control" type="text" name="searchText" id="searchText"
                                                                            placeholder="Type Search Criteria Here" />
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-2"></div>
                                <div class="col-md-6 text-center">
                                    <input type="submit" class="btn btn-default" />
                                </div>
                            </div>
                        </div>
                    </form>
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
