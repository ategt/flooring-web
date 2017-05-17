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
                <div class="col-md-6">
                    <%@ include file="../product/_list.jspf" %>
                </div>
                <div class="col-md-6">
                    <%@ include file="../state/_list.jspf" %>
                </div>
            </div>
        </div>
        <%@ include file="../partials/commonScript.jspf" %>
    </body>
</html>
