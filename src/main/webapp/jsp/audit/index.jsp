<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Audit Record</title>
        <%@ include file="../partials/commonHead.jspf" %>
    </head>
    <body>
        <div class="container">
            <h1>Audit Record</h1>
            <hr/>

            <%@ include file="../partials/banner.jspf" %>

            <div class="row">
                <div class="col-md-10 col-md-offset-1">
                    <%@ include file="_auditList.jspf" %>                    
                </div>
            </div>
        </div>

        <%@ include file="../partials/commonScript.jspf" %>

        <!-- Placed at the end of the document so the pages load faster -->
        <script src="${pageContext.request.contextPath}/js/auditapp.js"></script>
    </body>
</html>