<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="ROOT" scope="application"><%= request.getContextPath() %></c:set>
<c:set var="SELF" scope="application"><%=request.getRequestURI()%></c:set>
<c:set var="BORDER_COLOR" scope="application">#CCCCCC</c:set>

<link rel="stylesheet" type="text/css" href="${ROOT}/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${ROOT}/js/easyui/themes/icon.css">

<script type="text/javascript" src="${ROOT}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ROOT}/js/easyui/jquery.easyui.min.js"></script>
