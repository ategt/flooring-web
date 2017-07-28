<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Address Book</title>
        <%@ include file="../partials/commonHead.jspf" %>
    </head>
    <body>
        <div class="container">
            <h1>Address Book</h1>
            <hr/>
            <%@ include file="../partials/banner.jspf" %>

            <div class="row">

                <div class="col-md-6 text-center">
                    <h2>
                        ${address.firstName} ${address.lastName}<br />
                        <c:if test="${not empty address.company}">
                            ${address.company}<br />
                        </c:if>
                        ${address.streetNumber} ${address.streetName}<br />
                        ${address.city}, ${address.state} ${address.zip}<br />
                    </h2>
                    <div class="col-sm-12 text-center">
                        <a href="${pageContext.request.contextPath}/address/edit/${address.id}">Edit</a>
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
                        <c:if test="${not empty address.company}">
                            <tr>
                                <td>
                                    Company: 
                                </td>
                                <td>
                                    ${address.company}
                                </td>
                            </tr>
                        </c:if>
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

    <%@ include file="../partials/commonScript.jspf" %>        

</body>
</html>

