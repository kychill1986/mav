<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="ROOT" scope="application"><%= request.getContextPath() %></c:set>
<c:set var="SELF" scope="application"><%=request.getRequestURI()%></c:set>
<c:set var="BORDER_COLOR" scope="application">#CCCCCC</c:set>

<script type="text/javascript" src="${ROOT}/js/jquery-1.8.0.js"></script>