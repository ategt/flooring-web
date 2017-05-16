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
                <div class="col-md-6 col-md-push-6">
                    <%@ include file="_createAddressForm.jspf" %>                    
                </div>
                <div class="col-md-6 col-md-pull-6">
                    <%@ include file="_addressList.jspf" %>                    
                </div>
            </div>
        </div>

        <%@ include file="_showModal.jspf" %>
        <%@ include file="_editModal.jspf" %>

        <%@ include file="../partials/commonScript.jspf" %>

        <!-- Placed at the end of the document so the pages load faster -->
        <script src="${pageContext.request.contextPath}/js/addressapp.js"></script>
    </body>
</html>